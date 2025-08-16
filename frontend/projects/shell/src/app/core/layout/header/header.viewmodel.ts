import { computed, inject, Injectable } from '@angular/core'
import { Router } from '@angular/router'

import { redirectUrls } from '@ycyw/shell-core/auth/routes'
import { SessionStore } from '@ycyw/shell-shared/stores'
import { APP_CONFIG } from '@ycyw/shell-tokens/config-token'

@Injectable({
  providedIn: 'root',
  deps: [
    Router,
    SessionStore,
    APP_CONFIG,
  ],
})
export class HeaderViewModel {
  public readonly loggedIn = computed(() => this.sessionService.session().isLoggedIn)

  private readonly appConfig = inject(APP_CONFIG)
  private readonly sessionService = inject(SessionStore)

  get appName() {
    return this.appConfig.appName
  }

  public isHomePage(url: string): boolean {
    return url === redirectUrls.guestHomeUrl
  }

  public isAuthPage(url: string): boolean {
    return url === redirectUrls.login || url === redirectUrls.register
  }
}
