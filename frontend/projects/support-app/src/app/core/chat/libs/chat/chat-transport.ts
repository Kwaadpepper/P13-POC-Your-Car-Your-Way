export enum EventType {
  MESSAGE = 'message',
  PRESENCE = 'presence',
  TYPING = 'typing',
  HISTORY = 'history',
  JOIN = 'join',
  LEAVE = 'leave',
  ERROR = 'error',
}

interface ErrorEventPayload {
  code: string
  message: string
}

interface HistoryEvent {
  conversation: string
  messages: MessageEventPayload[]
}

interface JoinEventPayload {
  conversation: string
  participants: {
    user: string
    role: string
    status: string
  }[]
}

interface MessageEventPayload {
  id: string
  conversation: string
  from: {
    id: string
    role: string
  }
  text: string
  sentAt: string
}

interface PresenceEventPayload {
  user: string
  role: string
  status: string
  conversation: string
}

interface TypingEventPayload {
  user: string
  role: string
  conversation: string
  typing: boolean
}

export interface Events {
  [EventType.LEAVE]: {
    client: { conversation: string }
  }
  [EventType.PRESENCE]: {
    server: PresenceEventPayload
  }
  [EventType.MESSAGE]: {
    server: MessageEventPayload
    client: { conversation: string, text: string }
  }
  [EventType.TYPING]: {
    server: TypingEventPayload
    client: { conversation: string, isTyping: boolean }
  }
  [EventType.HISTORY]: {
    server: HistoryEvent
    client: { conversation: string, limit?: number }
  }
  [EventType.JOIN]: {
    server: JoinEventPayload
    client: { conversation: string }
  }
  [EventType.ERROR]: {
    server: ErrorEventPayload
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
  connect(): Promise<void>
  disconnect(): Promise<void>
  isConnected(): boolean

  send(cmd: ClientCommand): void

  onEvent(cb: (evt: ServerEvent) => void): () => void
}
