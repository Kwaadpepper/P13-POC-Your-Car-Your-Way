// Facade générique (sans Angular) pour manipuler le transport

import { ChatTransport, Message, ServerEvent } from './chat/chat-transport'
import { PresenceEvent, TypingEvent } from './chat/events'

type Unsub = () => void

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
    this.transport.send({ type: 'join', payload: { conversation } })
  }

  leave(conversation: string) {
    this.transport.send({ type: 'leave', payload: { conversation } })
  }

  sendMessage(conversation: string, text: string) {
    this.transport.send({ type: 'message', payload: { conversation, text } })
  }

  setTyping(conversation: string, isTyping: boolean) {
    this.transport.send({ type: 'typing', payload: { conversation, isTyping } })
  }

  getHistory(conversation: string, limit = 50) {
    this.transport.send({ type: 'getHistory', payload: { conversation, limit } })
  }

  onMessage(cb: (msg: Message) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === 'message') cb(evt.payload)
    })
  }

  onPresence(cb: (p: PresenceEvent) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === 'presence') cb(evt.payload)
    })
  }

  onTyping(cb: (t: TypingEvent) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === 'typing') cb(evt.payload)
    })
  }

  onHistory(cb: (messages: Message[]) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === 'history') cb(evt.payload.messages)
    })
  }
}
