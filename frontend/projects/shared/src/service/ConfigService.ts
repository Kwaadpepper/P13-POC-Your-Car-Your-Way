import { inject, Injectable } from '@angular/core'
import { APP_CONFIG, type ApplicationConfig } from '../config/ApplicationConfig.type'

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  private readonly appConfig = inject<ApplicationConfig>(APP_CONFIG)

  get config(): ApplicationConfig {
    return structuredClone(this.appConfig)
  }
}
