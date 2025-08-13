import { ConversationId, UserId } from '~support-domains/chat/models'
import { PresenceStatus, Role } from '~support-shared/enums'

export interface PresenceEvent {
  user: UserId
  role: Role
  status: PresenceStatus
  conversation: ConversationId
}
