import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { redirectUrls } from '~shell-core/auth/routes'
import { SessionStore } from '~shell-core/auth/stores'

@Injectable({
  providedIn: 'root',
  deps: [
    Router,
    SessionStore,
  ],
})
/** This is used to make sure that a logged in user cannot access guest routes */
export class GuestGuard implements CanActivate {
  private readonly redirectUrl = redirectUrls.authHomeUrl

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    // * If the user is logged in, redirect
    if (this.sessionStore.session().isLoggedIn) {
      const loginRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(loginRoute)
    }

    // * If the user is logged in, allow access
    return true
  }
}
