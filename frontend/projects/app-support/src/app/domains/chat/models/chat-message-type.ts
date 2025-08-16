import { Entity, UUID } from '@ycyw/shared'
import { Role } from '@ycyw/support-shared/enums'

export interface ChatMessage extends Entity {
  conversation: UUID
  from: {
    id: UUID
    name: string
    role: Role
  }
  text: string
  sentAt: Date
}
