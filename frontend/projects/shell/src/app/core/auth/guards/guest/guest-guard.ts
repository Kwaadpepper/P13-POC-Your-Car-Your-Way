import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'
import { redirectUrls } from '@shell-core/auth/routes'
import { SessionStore } from '@shell-core/auth/stores'

@Injectable({
  providedIn: 'root',
})
/** This is used to prevent non logged in user to access protected routes */
export class GuestGuard implements CanActivate {
  private readonly redirectUrl = redirectUrls.auth

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    // * If the user is not logged in, redirect to the login page
    if (!this.sessionStore.isLoggedIn()) {
      const loginRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(loginRoute)
    }

    // * If the user is logged in, allow access
    return true
  }
}
