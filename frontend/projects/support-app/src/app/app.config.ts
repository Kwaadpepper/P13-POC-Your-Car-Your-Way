import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import { ApplicationConfig, enableProdMode } from '@angular/core'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { provideRouter, withComponentInputBinding } from '@angular/router'

import { WebSocketTransport } from '~support-core/chat/libs'
import { CHAT_TRANSPORT } from '~support-core/chat/tokens'
import { APP_CONFIG, Configuration, primeNgProvider } from '~ycyw/shared'

import { environment } from '../environments/environment'

import { routes } from './app.routes'

export const configuration: Configuration = {
  ...{
    appName: 'Support App',
    version: '1.0.0',
  },
  environment: environment.env as Configuration['environment'],
}

const chatUrl = environment.chatWebSocketUrl

if (environment.env === 'production') {
  enableProdMode()
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideAnimationsAsync(),
    primeNgProvider,
    provideHttpClient(
      withInterceptorsFromDi(),
    ),
    {
      provide: APP_CONFIG,
      useValue: configuration,
    },
    {
      provide: CHAT_TRANSPORT,
      useValue: new WebSocketTransport(chatUrl),
    },
  ],
}
