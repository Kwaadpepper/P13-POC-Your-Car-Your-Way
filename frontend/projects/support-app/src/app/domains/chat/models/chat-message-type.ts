import { Entity, UUID } from '~ycyw/shared'

export interface ChatMessage extends Entity {
  conversation: UUID
  from: UUID
  text: string
  sentAt: Date
}
