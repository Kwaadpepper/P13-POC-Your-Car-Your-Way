import { ConversationId, UserId } from '@ycyw/support-domains/chat/models'
import { Role } from '@ycyw/support-shared/enums'

export interface TypingEvent {
  user: UserId
  role: Role
  conversation: ConversationId
  typing: boolean
}
