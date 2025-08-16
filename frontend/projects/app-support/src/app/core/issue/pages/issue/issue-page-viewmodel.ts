import { computed, inject, Injectable } from '@angular/core'

import { Role } from '@ycyw/support-shared/enums'
import { SessionStore } from '@ycyw/support-shared/stores'

@Injectable({
  providedIn: 'root',
})
export class IssuePageViewModel {
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
