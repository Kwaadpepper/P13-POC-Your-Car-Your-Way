import { inject, Injectable } from '@angular/core'

import { APP_CONFIG, type Configuration } from '../config/configuration'

@Injectable({
  providedIn: 'root',
})
export class ConfigStore {
  private readonly appConfig = inject<Configuration>(APP_CONFIG)

  get config(): Configuration {
    return structuredClone(this.appConfig)
  }
}
