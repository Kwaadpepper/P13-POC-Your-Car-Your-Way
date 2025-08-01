import { bootstrapApplication } from '@angular/platform-browser'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { provideRouter } from '@angular/router'
import { MessageService } from 'primeng/api'
import { providePrimeNG } from 'primeng/config'

import { provideZoneChangeDetection } from '@angular/core'
import { OpenClassrooms } from '@themes'
import { APP_CONFIG } from '@ycyw/shared'
import { AppComponent } from './app/app.component'
import { AppConfig } from './app/app.config'
import { routes } from './app/app.routes'

bootstrapApplication(AppComponent, {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimationsAsync(),
    providePrimeNG({
      ripple: false,
      theme: {
        preset: OpenClassrooms,
        options: {
          darkModeSelector: 'system',
          cssLayer: {
            name: 'primeng',
            order: 'tailwind-base, primeng, tailwind-utilities',
          },
        },
      },
    }),
    {
      provide: APP_CONFIG,
      useValue: AppConfig,
    },
    { provide: MessageService, useClass: MessageService },
  ],
}).catch(err => console.error(err))
