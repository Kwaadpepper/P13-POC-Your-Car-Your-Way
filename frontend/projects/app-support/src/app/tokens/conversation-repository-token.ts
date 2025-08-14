import { InjectionToken } from '@angular/core'

import { ConversationRepositoryImpl } from '~support-core/chat/repositories'
import { ConversationRepository } from '~support-domains/chat/repositories'

export const CONVERSATION_REPOSITORY = new InjectionToken<ConversationRepository>('ConversationRepositoryInjector', {
  providedIn: 'root',
  factory: () => new ConversationRepositoryImpl(),
})
