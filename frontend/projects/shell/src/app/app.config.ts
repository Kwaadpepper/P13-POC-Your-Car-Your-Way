import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import {
  ApplicationConfig,
  enableProdMode,
  ErrorHandler as NgErrorHandler,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { provideRouter, withComponentInputBinding } from '@angular/router'

import { MessageService } from 'primeng/api'

import { SessionInterceptor } from '~shell-core/auth/interceptors'
import { ErrorHandler } from '~shell-core/error-handler'
import { APP_CONFIG, Configuration, primeNgProvider } from '~ycyw/shared'

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
    provideRouter(routes, withComponentInputBinding()),
    provideAnimationsAsync(),
    primeNgProvider,
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
