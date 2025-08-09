import { PresenceStatus, Role } from '~support-domains/chat/enums'
import { ConversationId, UserId } from '~support-domains/chat/models'

export interface PresenceEvent {
  user: UserId
  role: Role
  status: PresenceStatus
  conversation: ConversationId
}
