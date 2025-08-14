import { InjectionToken } from '@angular/core'

import { ChatTransport, WebSocketTransport } from '~support-core/chat/libs'
import { environment } from '~support-env/environment'

export const CHAT_TRANSPORT = new InjectionToken<ChatTransport>('support.chat.transport', {
  providedIn: 'root',
  factory: () => new WebSocketTransport(environment.chatWebSocketUrl),
})
