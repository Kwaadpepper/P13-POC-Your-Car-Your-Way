import { inject, Injectable } from '@angular/core'
import { CanActivate, GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { redirectUrls } from '~shell-core/auth/routes'
import { Role } from '~shell-shared/enums'
import { SessionStore } from '~shell-shared/stores'

@Injectable({
  providedIn: 'root',
  deps: [
    Router,
    SessionStore,
  ],
})
/** This is used to make sure that a logged in user cannot access guest routes */
export class GuestGuard implements CanActivate {
  private readonly redirectUrlOperator = redirectUrls.authBackofficeHomeUrl
  private readonly redirectUrlClient = redirectUrls.authReservationHomeUrl

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    const session = this.sessionStore.session()
    // * If the user is logged in, allow access
    if (!session.isLoggedIn) return true

    // * If the user is logged in, redirect
    const loginRoute = session.user!.role === Role.CLIENT
      ? this.router.parseUrl(this.redirectUrlClient)
      : this.router.parseUrl(this.redirectUrlOperator)

    return new RedirectCommand(loginRoute)
  }
}
