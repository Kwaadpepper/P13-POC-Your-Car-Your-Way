import { randomUUID } from 'crypto'
import { createServer } from 'http'

import WebSocket, { RawData, WebSocketServer } from 'ws'

import {
  type ClientCommand,
  Events,
  EventType,
  type ServerEvent,
} from './../projects/app-support/src/app/core/chat/libs/chat/chat-transport'

interface Message {
  id: string
  conversation: string
  from: {
    id: string
    name: string
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
  // Déduit le rôle via le token (ex: "client_...", "operator_...")
  const role = token.startsWith('operator') ? 'operator' : 'client'
  const user = token.split('_')[1] || randomUUID()

  ws.user = user
  ws.role = role
  ws.joined = new Set() // roomId

  const events = {
    joined: (payload: Events[EventType.JOIN]['client']) => {
      const { conversation } = payload
      ensureRoom(conversation)
      const room = rooms.get(conversation)!

      room.add(ws)
      ws.joined.add(conversation)

      // Envoie au nouveau venu la liste des participants (version JOIN d'origine)
      const participants = [...room]
        .filter(p => p.user !== user) // Exclut l'expéditeur
        .map(s => ({
          user: s.user,
          role: s.role,
          status: 'online',
        }))
      const participantsEvent: ServerEvent = {
        type: EventType.JOIN,
        payload: { conversation, participants } as Events[EventType.JOIN]['server'],
      }
      ws.send(JSON.stringify(participantsEvent))

      // En plus: envoie au nouveau venu des PRESENCE "online" pour chaque participant déjà présent
      for (const other of room) {
        if (other === ws) continue
        const presenceForNewcomer: ServerEvent = {
          type: EventType.PRESENCE,
          payload: {
            user: other.user,
            role: other.role,
            status: 'online',
            conversation,
          } as Events[EventType.PRESENCE]['server'],
        }
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify(presenceForNewcomer))
        }
      }

      // Signale présence "online" du nouvel arrivant aux autres
      const presenceOnlineNewUser: ServerEvent = {
        type: EventType.PRESENCE,
        payload: {
          user,
          role,
          status: 'online',
          conversation,
        } as Events[EventType.PRESENCE]['server'],
      }
      broadcast(conversation, presenceOnlineNewUser, ws)

      // Diffuse l'événement JOIN aux autres (inchangé)
      const joinForOthersPayload: Events[EventType.JOIN]['server'] = {
        conversation,
        participants: [{ user, role, status: 'online' }],
      }
      const joinForOthers: ServerEvent = { type: EventType.JOIN, payload: joinForOthersPayload }
      broadcast(conversation, joinForOthers, ws)
    },

    leave: (payload: Events[EventType.LEAVE]['client']) => {
      const { conversation } = payload
      const room = rooms.get(conversation)
      if (!room) return

      room.delete(ws)
      ws.joined.delete(conversation)

      // PRESENCE "offline" pour notifier la room
      const eventPayload: Events[EventType.PRESENCE]['server'] = {
        user,
        role,
        status: 'offline',
        conversation,
      }
      const event: ServerEvent = { type: EventType.PRESENCE, payload: eventPayload }
      broadcast(conversation, event)
    },

    message: (payload: Events[EventType.MESSAGE]['client']) => {
      const { conversation, text } = payload
      const roomId = conversation
      ensureRoom(roomId)

      const chatMsg: Message = {
        id: randomUUID(),
        conversation: roomId,
        from: { id: user, name: role === 'operator' ? 'Randy' : 'Susy', role },
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
    },

    typing: (payload: Events[EventType.TYPING]['client']) => {
      const { conversation, isTyping } = payload
      const roomId = conversation

      const eventPayload: Events[EventType.TYPING]['server'] = {
        user,
        role,
        conversation: roomId,
        typing: isTyping,
      }
      const event: ServerEvent = { type: EventType.TYPING, payload: eventPayload }

      broadcast(roomId, event, ws)
    },

    history: (payload: Events[EventType.HISTORY]['client']) => {
      const { conversation, limit = 50 } = payload
      const all = messagesByRoom.get(conversation) || []
      const slice = all.slice(-limit)

      const eventPayload: Events[EventType.HISTORY]['server'] = {
        conversation,
        messages: slice,
      }
      const event: ServerEvent = { type: EventType.HISTORY, payload: eventPayload }

      ws.send(JSON.stringify(event))
    },

    dispatchEvent(evt: EventType, payload: ClientCommand['payload']) {
      switch (evt) {
        case EventType.JOIN:
          events.joined(payload as Events[EventType.JOIN]['client'])
          break
        case EventType.LEAVE:
          events.leave(payload as Events[EventType.LEAVE]['client'])
          break
        case EventType.MESSAGE:
          events.message(payload as Events[EventType.MESSAGE]['client'])
          break
        case EventType.TYPING:
          events.typing(payload as Events[EventType.TYPING]['client'])
          break
        case EventType.HISTORY:
          events.history(payload as Events[EventType.HISTORY]['client'])
          break
      }
    },
  }

  // IMPORTANT: pas de ws.on('open') côté serveur (cet event n'est pas émis côté serveur)

  ws.on('message', (raw: RawData) => {
    let msg: ClientCommand
    try {
      msg = JSON.parse(raw.toString())
    }
    catch {
      return
    }
    const { type, payload } = msg || {}

    events.dispatchEvent(type, payload)
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
    ws.joined.clear()
  })
})

const PORT = process.env.PORT || 7071
server.listen(PORT, () => console.log(`WS mock server on ws://localhost:${PORT}`))
