import { computed, inject, Injectable, signal } from '@angular/core'
import { Router } from '@angular/router'

import { SessionService } from '@core/services'
import { redirectUrls } from '@routes'
import { ConfigService } from '@ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [Router, SessionService],
})
export class HeaderViewModel {
  public readonly appName = signal<string>('')

  public readonly loggedIn = computed(() => this.sessionService.isLoggedIn())

  private readonly configService = inject(ConfigService)
  private readonly sessionService = inject(SessionService)

  constructor() {
    const appConfig = this.configService.config

    this.appName.set(appConfig.appName)
  }

  public isHomePage(url: string): boolean {
    return url === redirectUrls.home
  }

  public isAuthPage(url: string): boolean {
    return url === redirectUrls.login || url === redirectUrls.register
  }
}
