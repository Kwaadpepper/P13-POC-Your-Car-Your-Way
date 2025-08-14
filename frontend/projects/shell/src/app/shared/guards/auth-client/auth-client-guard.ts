import { inject, Injectable } from '@angular/core'
import { GuardResult, MaybeAsync, RedirectCommand, Router } from '@angular/router'

import { redirectUrls } from '~shell-core/auth/routes'
import { Role } from '~shell-shared/enums'
import { SessionStore } from '~shell-shared/stores'

import { AuthGuard } from '../auth/auth-guard'

/** This is used to make sure that a non logged in user cannot access protected routes */
@Injectable({
  providedIn: 'root',
  deps: [
    Router,
    SessionStore,
  ],
})
export class AuthClientGuard implements AuthGuard {
  readonly redirectUrl = redirectUrls.guestHomeUrl

  private readonly router = inject(Router)
  private readonly sessionStore = inject(SessionStore)

  canActivate(): MaybeAsync<GuardResult> {
    const session = this.sessionStore.session()
    if (
      !session.isLoggedIn
      || session.user!.role !== Role.CLIENT
    ) {
      const unAuthRoute = this.router.parseUrl(this.redirectUrl)

      return new RedirectCommand(unAuthRoute)
    }

    return true
  }
}
