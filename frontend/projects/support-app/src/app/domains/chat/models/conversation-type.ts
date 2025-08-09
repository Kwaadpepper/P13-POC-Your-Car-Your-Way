import { Entity } from '~ycyw/shared'

export interface Conversation extends Entity {
  subject: string
}

export type ConversationId = Conversation['id']
