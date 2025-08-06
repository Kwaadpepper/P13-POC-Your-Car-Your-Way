import { inject, Injectable, signal } from '@angular/core'
import { ConfigStore } from '@ycyw/shared'

@Injectable({
  providedIn: 'root',
})
export class HomeViewModel {
  public readonly appName = signal<string>('')
  private readonly configStore = inject(ConfigStore)

  constructor() {
    const appConfig = this.configStore.config

    this.appName.set(appConfig.appName)
  }
}
