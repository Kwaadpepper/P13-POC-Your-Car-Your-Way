import { Entity } from '~ycyw/shared'

export interface Conversation extends Entity {
  subject: string
  lastMessage?: {
    content: string
    sentAt: Date
  }
}

export type ConversationId = Conversation['id']
