import { randomUUID } from 'crypto'
import { createServer } from 'http'

import WebSocket, { RawData, WebSocketServer } from 'ws'

import {
  type ClientCommand,
  Events,
  EventType,
  type ServerEvent,
} from './../projects/support-app/src/app/core/chat/libs/chat/chat-transport'

interface Message {
  id: string
  conversation: string
  from: {
    id: string
    role: Role
  }
  text: string
  sentAt: string
}

type Role = 'operator' | 'client'
type Client = {
  user: string
  role: Role
  joined: Set<string>
} & WebSocket

const server = createServer()
const wss = new WebSocketServer({ server })

/** Mémoire */
const rooms = new Map<string, Set<Client>>()
const messagesByRoom = new Map<string, Message[]>()

function broadcast(roomId: string, msg: ServerEvent, excludeWs?: Client) {
  const clients = rooms.get(roomId)
  if (!clients) return
  for (const ws of clients) {
    if (ws.readyState === WebSocket.OPEN && ws !== excludeWs) {
      ws.send(JSON.stringify(msg))
    }
  }
}

function ensureRoom(roomId: string) {
  if (!rooms.has(roomId)) rooms.set(roomId, new Set())
  if (!messagesByRoom.has(roomId)) messagesByRoom.set(roomId, [])
}

wss.on('connection', (ws: Client, req) => {
  const url = new URL(req.url || '', 'http://localhost')
  const token = url.searchParams.get('token') || ''
  // Déduit le rôle via le token (ex: "client_7b8e42ff-4471-429d-9f1a-cb3b220cdb16", "operator_7b8e42ff-4471-429d-9f1a-cb3b220cdb16"
  const role = token.startsWith('operator') ? 'operator' : 'client'
  const user = token.split('_')[1]

  ws.user = user
  ws.role = role
  ws.joined = new Set() // roomId

  ws.on('message', (raw: RawData) => {
    let msg: ClientCommand
    try {
      msg = JSON.parse(raw.toString())
    }
    catch {
      return
    }
    const { type, payload } = msg || {}

    if (type === EventType.JOIN) {
      const { conversation } = payload
      ensureRoom(conversation)
      const room = rooms.get(conversation)!

      room.add(ws)
      ws.joined.add(conversation)

      // Envoie au nouveau venu la liste des participants
      const participants = [...room].map(s => ({
        user: s.user,
        role: s.role,
        status: 'online',
      }))
      const participantsEvent = { type: EventType.JOIN, payload: { conversation, participants } }
      ws.send(JSON.stringify(participantsEvent))

      // Signale présence aux autres
      const eventPayload: Events[EventType.JOIN]['server'] = {
        conversation,
        participants: [{ user, role, status: 'online' }],
      }
      const event: ServerEvent = { type: EventType.JOIN, payload: eventPayload }

      broadcast(conversation, event, ws)
    }
    else if (type === EventType.LEAVE) {
      const roomId = payload.conversation
      rooms.get(roomId)?.delete(ws)
      ws.joined.delete(roomId)

      const eventPayload: Events[EventType.PRESENCE]['server'] = {
        user,
        role,
        status: 'offline',
        conversation: roomId,
      }
      const event: ServerEvent = { type: EventType.PRESENCE, payload: eventPayload }

      broadcast(roomId, event)
    }
    else if (type === EventType.MESSAGE) {
      const { conversation, text } = payload
      const roomId = conversation
      ensureRoom(roomId)

      const chatMsg: Message = {
        id: randomUUID(),
        conversation: roomId,
        from: { id: user, role },
        text,
        sentAt: new Date().toISOString(),
      }
      messagesByRoom.get(roomId)!.push(chatMsg)

      const event: ServerEvent = { type: EventType.MESSAGE, payload: chatMsg }

      // Fanout à tous (y compris l'expéditeur pour avoir l'horodatage/uniformité)
      broadcast(roomId, event, ws)

      // Envoie à l'expéditeur une seule fois
      if (ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify(event))
      }
    }
    else if (type === EventType.TYPING) {
      const { conversation, isTyping } = payload
      const roomId = conversation

      const eventPayload: Events[EventType.TYPING]['server'] = {
        user,
        role,
        conversation: roomId,
        typing: isTyping,
      }
      const event: ServerEvent = { type: EventType.TYPING, payload: eventPayload }

      broadcast(conversation, event, ws)
    }
    else if (type === EventType.HISTORY) {
      const { conversation, limit = 50 } = payload
      const all = messagesByRoom.get(conversation) || []
      const slice = all.slice(-limit)

      const eventPayload: Events[EventType.HISTORY]['server'] = {
        conversation,
        messages: slice,
      }
      const event: ServerEvent = { type: EventType.HISTORY, payload: eventPayload }

      ws.send(JSON.stringify(event))
    }
  })

  ws.on('close', () => {
    for (const roomId of ws.joined) {
      rooms.get(roomId)?.delete(ws)

      const eventPayload: Events[EventType.PRESENCE]['server'] = {
        user,
        role,
        status: 'offline',
        conversation: roomId,
      }
      const event: ServerEvent = { type: EventType.PRESENCE, payload: eventPayload }

      broadcast(roomId, event)
    }
  })
})

const PORT = process.env.PORT || 7071
server.listen(PORT, () => console.log(`WS mock server on ws://localhost:${PORT}`))
