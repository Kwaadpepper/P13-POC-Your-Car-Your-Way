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

import { ErrorHandler } from '~shell-core/error-handler'
import { SessionInterceptor } from '~shell-core/interceptors'
import { configuration } from '~shell-tokens/config-token'
import { primeNgProvider } from '~ycyw/shared'

import { routes } from './app.routes'

if (configuration.environment === 'production') {
  enableProdMode()
}

export const appConfig: ApplicationConfig = {
  providers: [
    // Angular providers
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptorsFromDi(),
    ),

    // Shared providers
    primeNgProvider,

    // Custom providers
    { provide: MessageService, useClass: MessageService },
    { provide: NgErrorHandler, useClass: ErrorHandler },
    { provide: HTTP_INTERCEPTORS, useClass: SessionInterceptor, multi: true },
  ],
}
