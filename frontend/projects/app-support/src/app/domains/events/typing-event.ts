import { ConversationId, UserId } from '~support-domains/chat/models'
import { Role } from '~support-shared/enums'

export interface TypingEvent {
  user: UserId
  role: Role
  conversation: ConversationId
  typing: boolean
}
