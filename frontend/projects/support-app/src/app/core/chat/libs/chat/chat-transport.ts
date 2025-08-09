import { PresenceEvent, TypingEvent } from './events'

export interface Message {
  id: string
  conversation: string
  from: string
  text: string
  sentAt: string
}

export type ServerEvent
  = | { type: 'message', payload: Message }
    | { type: 'presence', payload: PresenceEvent }
    | { type: 'typing', payload: TypingEvent }
    | {
      type: 'history'
      payload: {
        conversation: string
        messages: Message[]
      }
    }
    | {
      type: 'joined'
      payload: {
        conversation: string
        participants: { user: string, role: string, status: string }[]
      }
    }
    | { type: 'error', payload: { code: string, message: string } }

export type ClientCommand
  = | { type: 'join', payload: { conversation: string } }
    | { type: 'leave', payload: { conversation: string } }
    | { type: 'message', payload: { conversation: string, text: string } }
    | { type: 'typing', payload: { conversation: string, isTyping: boolean } }
    | { type: 'getHistory', payload: { conversation: string, limit?: number } }

export interface ChatTransport {
  connect(token?: string): Promise<void>
  disconnect(): Promise<void>
  isConnected(): boolean

  send(cmd: ClientCommand): void

  onEvent(cb: (evt: ServerEvent) => void): () => void
}
