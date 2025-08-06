import { ApplicationConfig, enableProdMode, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core'
import { provideRouter } from '@angular/router'
import { OpenClassrooms } from '@themes'
import { APP_CONFIG, Configuration } from '@ycyw/shared'
import { providePrimeNG } from 'primeng/config'

import { environment } from './../environments/environment'
import { routes } from './app.routes'
import config from './application.json'

export const AppConfig: Configuration = {
  ...config,
  environment: environment.env as Configuration['environment'],
}

if (AppConfig.environment === 'production') {
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
    {
      provide: APP_CONFIG,
      useValue: AppConfig,
    },
  ],
}
