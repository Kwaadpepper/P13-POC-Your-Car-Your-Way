import { Entity } from '~ycyw/shared'

export interface Conversation extends Entity {
  subject: string
  lastMessage: {
    content: string
    date: Date
  }
}

export type ConversationId = Conversation['id']
