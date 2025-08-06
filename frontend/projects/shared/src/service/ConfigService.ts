import { inject, Injectable } from '@angular/core'
import { APP_CONFIG, type Configuration } from '../config/configuration'

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  private readonly appConfig = inject<Configuration>(APP_CONFIG)

  get config(): Configuration {
    return structuredClone(this.appConfig)
  }
}
