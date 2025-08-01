import { inject, Injectable, signal } from '@angular/core'

import { ConfigService } from '@ycyw/shared'

@Injectable({
  providedIn: 'root',
})
export class HomeViewModel {
  public readonly appName = signal<string>('')
  private readonly configService = inject(ConfigService)

  constructor() {
    const appConfig = this.configService.config

    this.appName.set(appConfig.appName)
  }
}
