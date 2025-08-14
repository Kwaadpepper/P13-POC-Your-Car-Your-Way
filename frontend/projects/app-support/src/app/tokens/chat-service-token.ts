import { InjectionToken } from '@angular/core'

import { ChatServiceImpl } from '~support-core/chat/services'
import { ChatService } from '~support-domains/chat/services'

export const CHAT_SERVICE = new InjectionToken<ChatService>('ChatServiceInjector', {
  providedIn: 'root',
  factory: () => new ChatServiceImpl(),
})
