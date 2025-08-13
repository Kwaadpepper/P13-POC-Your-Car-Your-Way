import { Injectable, computed, inject } from '@angular/core'

import { Role } from '~support-shared/enums'
import { SessionBroadcastService } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [
    SessionBroadcastService,
  ],
})
export class ConversationViewModel {
  private readonly sessionBus = inject(SessionBroadcastService)

  readonly currentUser = computed(() => {
    console.log(this.sessionBus)
    const u = this.sessionBus.user
    if (!u) return null
    const name = u.name
    const isOperator = u.role === Role.OPERATOR
    return {
      id: u.id,
      name,
      isOperator,
    }
  })
}
