import { PresenceEvent, TypingEvent } from './events'

export interface Message {
  id: string
  conversation: string
  from: {
    id: string
    role: string
  }
  text: string
  sentAt: string
}

export enum EventType {
  MESSAGE = 'message',
  PRESENCE = 'presence',
  TYPING = 'typing',
  HISTORY = 'history',
  JOIN = 'join',
  LEAVE = 'leave',
  ERROR = 'error',
}

export interface Events {
  [EventType.LEAVE]: {
    client: { conversation: string }
  }
  [EventType.PRESENCE]: {
    server: PresenceEvent
  }
  [EventType.MESSAGE]: {
    server: Message
    client: { conversation: string, text: string }
  }
  [EventType.TYPING]: {
    server: TypingEvent
    client: { conversation: string, isTyping: boolean }
  }
  [EventType.HISTORY]: {
    server: { conversation: string, messages: Message[] }
    client: { conversation: string, limit?: number }
  }
  [EventType.JOIN]: {
    server: { conversation: string, participants: { user: string, role: string, status: string }[] }
    client: { conversation: string }
  }
  [EventType.ERROR]: {
    server: { code: string, message: string }
  }
}

export type ServerEvent
  = | { type: EventType.PRESENCE, payload: Events[EventType.PRESENCE]['server'] }
    | { type: EventType.MESSAGE, payload: Events[EventType.MESSAGE]['server'] }
    | { type: EventType.TYPING, payload: Events[EventType.TYPING]['server'] }
    | { type: EventType.HISTORY, payload: Events[EventType.HISTORY]['server'] }
    | { type: EventType.JOIN, payload: Events[EventType.JOIN]['server'] }
    | { type: EventType.ERROR, payload: Events[EventType.ERROR]['server'] }

export type ClientCommand
= | { type: EventType.LEAVE, payload: Events[EventType.LEAVE]['client'] }
  | { type: EventType.MESSAGE, payload: Events[EventType.MESSAGE]['client'] }
  | { type: EventType.TYPING, payload: Events[EventType.TYPING]['client'] }
  | { type: EventType.HISTORY, payload: Events[EventType.HISTORY]['client'] }
  | { type: EventType.JOIN, payload: Events[EventType.JOIN]['client'] }

export interface ChatTransport {
  connect(token?: string): Promise<void>
  disconnect(): Promise<void>
  isConnected(): boolean

  send(cmd: ClientCommand): void

  onEvent(cb: (evt: ServerEvent) => void): () => void
}
