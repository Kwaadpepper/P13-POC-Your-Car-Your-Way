import { InjectionToken } from '@angular/core'

import { ChatTransport } from '../libs'

export const CHAT_TRANSPORT = new InjectionToken<ChatTransport>('app.config')
