import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { redirectUrls } from '~shell-core/auth/routes'
import { SessionStore } from '~shell-core/auth/stores'

@Injectable({
  providedIn: 'root',
})
/** This is used to make sure that a non logged in user cannot access protected routes */
export class AuthGuard implements CanActivate {
  private readonly redirectUrl = redirectUrls.guestHomeUrl

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    // * If the user is not logged in, redirect
    if (!this.sessionStore.isLoggedIn()) {
      const loginRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(loginRoute)
    }

    // * If the user is not logged in, allow access
    return true
  }
}
