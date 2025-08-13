import { Entity, UUID } from '~ycyw/shared'

import { Role } from '../enums'

export interface ChatMessage extends Entity {
  conversation: UUID
  from: {
    id: UUID
    role: Role
  }
  text: string
  sentAt: Date
}
