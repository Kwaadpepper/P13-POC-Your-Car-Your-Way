import { ConversationId, UserId } from '@ycyw/support-domains/chat/models'
import { PresenceStatus, Role } from '@ycyw/support-shared/enums'

export interface PresenceEvent {
  user: UserId
  role: Role
  status: PresenceStatus
  conversation: ConversationId
}
