import { inject, Injectable } from '@angular/core'
import { APP_CONFIG, type ApplicationConfig } from '../config/ApplicationConfig.type'

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  private readonly config = inject<ApplicationConfig>(APP_CONFIG)

  get getConfig(): ApplicationConfig {
    return this.config
  }
}
