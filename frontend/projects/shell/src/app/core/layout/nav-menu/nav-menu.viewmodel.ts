import { computed, inject, Injectable } from '@angular/core'

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
}
