import { Injectable, computed, inject } from '@angular/core'

import { Role } from '~support-shared/enums'
import { SessionStore } from '~support-shared/stores'
import { SessionBroadcastService } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [
    SessionBroadcastService,
  ],
})
export class ConversationViewModel {
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
