import { computed, inject, Injectable, signal } from '@angular/core'
import { Router } from '@angular/router'

import { redirectUrls } from '~shell-core/auth/routes'
import { SessionStore } from '~shell-core/auth/stores'
import { ConfigStore } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [Router, SessionStore],
})
export class HeaderViewModel {
  public readonly appName = signal<string>('')

  public readonly loggedIn = computed(() => this.sessionService.isLoggedIn())

  private readonly configService = inject(ConfigStore)
  private readonly sessionService = inject(SessionStore)

  constructor() {
    const appConfig = this.configService.config

    this.appName.set(appConfig.appName)
  }

  public isHomePage(url: string): boolean {
    return url === redirectUrls.guestHomeUrl
  }

  public isAuthPage(url: string): boolean {
    return url === redirectUrls.login || url === redirectUrls.register
  }
}
