import { InjectionToken } from '@angular/core'

import { ChatServiceImpl } from '@ycyw/support-core/chat/services'
import { ChatService } from '@ycyw/support-domains/chat/services'

export const CHAT_SERVICE = new InjectionToken<ChatService>('ChatServiceInjector', {
  providedIn: 'root',
  factory: () => new ChatServiceImpl(),
})
