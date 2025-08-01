import { platformBrowser } from '@angular/platform-browser'
import { APP_CONFIG } from '@ycyw/shared'
import { AppModule } from './app/app-module'
import { AppConfig } from './app/app.config'

platformBrowser()
  .bootstrapModule(AppModule, {
    ngZoneEventCoalescing: true,
    providers: [
      {
        provide: APP_CONFIG,
        useValue: AppConfig,
      },
    ],
  })
  .catch(err => console.error(err))
