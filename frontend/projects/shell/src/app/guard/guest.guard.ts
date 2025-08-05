import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { SessionService } from '@core/services'
import { redirectUrls } from '@routes'

@Injectable({
  providedIn: 'root',
})
/** This is used to make sure that a logged in user will stay within the protected routes */
export class GuestGuard implements CanActivate {
  private readonly redirectUrl = redirectUrls.posts

  private readonly router = inject(Router)
  private readonly sessionService = inject(SessionService)

  canActivate(): MaybeAsync<GuardResult> {
    // * If the user is logged in, redirect to the posts page
    if (this.sessionService.isLoggedIn()) {
      const loginRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(loginRoute)
    }

    // * If the user is not logged in, allow access
    return true
  }
}
