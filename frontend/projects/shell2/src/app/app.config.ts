import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core'
import { provideRouter } from '@angular/router'
import { providePrimeNG } from 'primeng/config'

import { OpenClassrooms } from '@themes'
import { APP_CONFIG, Configuration } from '@ycyw/shared'
import { routes } from './app.routes'
import config from './application.json'
import { environment } from './environment/environment'

export const AppConfig: Configuration = {
  ...config,
  environment: environment.env,
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
