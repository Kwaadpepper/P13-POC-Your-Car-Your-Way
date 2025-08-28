import { InjectionToken } from '@angular/core'

import { ChatTransport, StompWebSocketTransport } from '@ycyw/support-core/chat/libs'
import { environment } from '@ycyw/support-env/environment'

export const CHAT_TRANSPORT = new InjectionToken<ChatTransport>('support.chat.transport', {
  providedIn: 'root',
  factory: () => new StompWebSocketTransport(environment.chatWebSocketUrl),
})
