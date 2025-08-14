import { Role } from '~support-shared/enums'
import { Entity, UUID } from '~ycyw/shared'

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
