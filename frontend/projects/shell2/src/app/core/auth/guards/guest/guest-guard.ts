import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { redirectUrls } from '@shell2-core/auth/routes'
import { SessionStore } from '@shell2-core/auth/stores'

@Injectable({
  providedIn: 'root',
})
/** This is used to make sure that a logged in user will stay within the protected routes */
export class GuestGuard implements CanActivate {
  private readonly redirectUrl = redirectUrls.posts

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    // * If the user is logged in, redirect to the posts page
    if (this.sessionStore.isLoggedIn()) {
      const loginRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(loginRoute)
    }

    // * If the user is not logged in, allow access
    return true
  }
}
