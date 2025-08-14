import { Injectable, computed, inject } from '@angular/core'

import { Role } from '~backoffice-shared/enums'
import { SessionStore } from '~backoffice-shared/stores'

@Injectable({
  providedIn: 'root',
  deps: [
    SessionStore,
  ],
})
export class DashboardViewModel {
  private readonly bus = inject(SessionStore)

  readonly currentUser = computed(() => {
    const user = this.bus.session().user
    if (!user) return null
    const name = user.name
    const isOperator = user.role === Role.OPERATOR
    return {
      id: user.id,
      name,
      isOperator,
    }
  })
}
