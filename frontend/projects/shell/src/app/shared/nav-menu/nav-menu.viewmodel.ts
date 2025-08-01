import { computed, inject, Injectable } from '@angular/core'

import { SessionService } from '@core/services'

@Injectable({
  providedIn: 'root',
  deps: [SessionService],
})
export class NavMenuViewModel {
  private readonly sessionService = inject(SessionService)

  public readonly loggedIn = computed(() => this.sessionService.isLoggedIn())
}
