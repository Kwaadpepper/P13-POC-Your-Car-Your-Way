import { computed, inject, Injectable } from '@angular/core'

import { Role } from '~shell-shared/enums'
import { SessionStore } from '~shell-shared/stores'

@Injectable({
  providedIn: 'root',
  deps: [
    SessionStore,
  ],
})
export class NavMenuViewModel {
  private readonly sessionStore = inject(SessionStore)

  public readonly loggedIn = computed(() => this.sessionStore.session().isLoggedIn)

  public readonly supportUrl = computed(() => {
    const session = this.sessionStore.session()
    return this.loggedIn() && session.user!.role === Role.CLIENT
      ? '/reservation/support'
      : '/backoffice/support'
  })
}
