import { bootstrapApplication } from '@angular/platform-browser'
import { provideRouter } from '@angular/router'
import { APP_CONFIG } from '@ycyw/shared'

import { App } from './app/app.component'
import { AppConfig } from './app/app.config'
import { routes } from './app/app.routes'

bootstrapApplication(App, {
  providers: [
    provideRouter(routes),
    {
      provide: APP_CONFIG,
      useValue: AppConfig,
    },
  ],
}).catch(err => console.error(err))
