import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http'
import { ApplicationConfig, enableProdMode } from '@angular/core'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { provideRouter, withComponentInputBinding } from '@angular/router'

import { primeNgProvider } from '~ycyw/shared'

import { environment } from '../environments/environment'

import { routes } from './app.routes'

if (environment.env === 'production') {
  enableProdMode()
}

export const appConfig: ApplicationConfig = {
  providers: [
    primeNgProvider,

    // Normal Angular providers (that shell also uses)
    provideRouter(routes, withComponentInputBinding()),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptorsFromDi(),
    ),
  ],
}
