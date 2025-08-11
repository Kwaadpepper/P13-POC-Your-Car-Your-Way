// Facade générique (sans Angular) pour manipuler le transport

import { ChatTransport, Events, EventType, ServerEvent } from './chat/chat-transport'

type Unsub = () => void

export type MessageEventPayload = Events[EventType.MESSAGE]['server']
export type PresenceEventPayload = Events[EventType.PRESENCE]['server']
export type TypingEventPayload = Events[EventType.TYPING]['server']
export type HistoryEventPayload = Events[EventType.HISTORY]['server']

export class ChatClient {
  private readonly transport: ChatTransport

  constructor(transport: ChatTransport) {
    this.transport = transport
  }

  async connect(token?: string) {
    await this.transport.connect(token)
  }

  disconnect() {
    return this.transport.disconnect()
  }

  join(conversation: string) {
    this.transport.send({ type: EventType.JOIN, payload: { conversation } })
  }

  leave(conversation: string) {
    this.transport.send({ type: EventType.LEAVE, payload: { conversation } })
  }

  sendMessage(conversation: string, text: string) {
    this.transport.send({ type: EventType.MESSAGE, payload: { conversation, text } })
  }

  setTyping(conversation: string, isTyping: boolean) {
    this.transport.send({ type: EventType.TYPING, payload: { conversation, isTyping } })
  }

  getHistory(conversation: string, limit = 50) {
    this.transport.send({ type: EventType.HISTORY, payload: { conversation, limit } })
  }

  onMessage(cb: (m: MessageEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.MESSAGE) cb(evt.payload)
    })
  }

  onPresence(cb: (p: PresenceEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.PRESENCE) cb(evt.payload)
    })
  }

  onTyping(cb: (t: TypingEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.TYPING) cb(evt.payload)
    })
  }

  onHistory(cb: (h: HistoryEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.HISTORY) cb(evt.payload)
    })
  }
}
