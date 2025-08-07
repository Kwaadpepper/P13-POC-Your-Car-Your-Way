import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import {
  ApplicationConfig,
  enableProdMode,
  ErrorHandler as NgErrorHandler,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core'
import { provideRouter } from '@angular/router'
import { SessionInterceptor } from '@shell-core/auth/interceptors'
import { ErrorHandler } from '@shell-core/error-handler'
import { OpenClassrooms } from '@themes'
import { APP_CONFIG, Configuration } from '@ycyw/shared'
import { MessageService } from 'primeng/api'
import { providePrimeNG } from 'primeng/config'

import { environment } from './../environments/environment'
import { routes } from './app.routes'
import config from './application.json'

export const configuration: Configuration = {
  ...config,
  environment: environment.env as Configuration['environment'],
}

if (configuration.environment === 'production') {
  enableProdMode()
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    providePrimeNG({
      ripple: false,
      theme: {
        preset: OpenClassrooms,
        options: {
          darkModeSelector: 'system',
          cssLayer: {
            name: 'primeng',
            order: 'theme,base,components,utilities,plugins,primeng',
          },
        },
      },
    }),
    provideHttpClient(
      withInterceptorsFromDi(),
    ),
    { provide: MessageService, useClass: MessageService },
    {
      provide: APP_CONFIG,
      useValue: configuration,
    },
    { provide: NgErrorHandler, useClass: ErrorHandler },
    { provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true },
  ],
}
