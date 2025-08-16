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

import { primeNgProvider } from '@ycyw/shared'
import { ErrorHandler } from '@ycyw/shell-core/error-handler'
import { SessionInterceptor } from '@ycyw/shell-core/interceptors'
import { configuration } from '@ycyw/shell-tokens/config-token'

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
