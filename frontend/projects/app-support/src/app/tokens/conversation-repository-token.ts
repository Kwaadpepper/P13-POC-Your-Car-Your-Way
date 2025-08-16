import { InjectionToken } from '@angular/core'

import { ConversationRepositoryImpl } from '@ycyw/support-core/chat/repositories'
import { ConversationRepository } from '@ycyw/support-domains/chat/repositories'

export const CONVERSATION_REPOSITORY = new InjectionToken<ConversationRepository>('ConversationRepositoryInjector', {
  providedIn: 'root',
  factory: () => new ConversationRepositoryImpl(),
})
