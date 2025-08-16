import { inject, Injectable } from '@angular/core'

import { APP_CONFIG } from '@ycyw/shell-tokens/config-token'

@Injectable({
  providedIn: 'root',
  deps: [APP_CONFIG],
})
export class HomeViewModel {
  private readonly appConfig = inject(APP_CONFIG)

  get appName() {
    return this.appConfig.appName
  }
}
