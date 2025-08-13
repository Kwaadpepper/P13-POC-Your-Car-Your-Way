import { Observable } from 'rxjs'

import { PresenceEvent, TypingEvent } from '~support-domains/events'

import { ChatMessage, ConversationId } from '../models'

export interface ChatService {

  readonly messages$: Observable<ChatMessage>
  readonly presence$: Observable<PresenceEvent>
  readonly typing$: Observable<TypingEvent>
  readonly history$: Observable<ChatMessage[]>

  connect(): Promise<void>

  disconnect(): Promise<void>

  join(conversationId: ConversationId): void

  leave(conversationId: ConversationId): void

  sendMessage(conversationId: ConversationId, text: string): void

  setTyping(conversationId: ConversationId, isTyping: boolean): void

  getHistory(conversationId: ConversationId, limit: number): void
}
