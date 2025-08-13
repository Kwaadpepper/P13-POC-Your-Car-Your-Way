import { Role } from '~support-domains/chat/enums'
import { ConversationId, UserId } from '~support-domains/chat/models'

export interface TypingEvent {
  user: UserId
  role: Role
  conversation: ConversationId
  typing: boolean
}
