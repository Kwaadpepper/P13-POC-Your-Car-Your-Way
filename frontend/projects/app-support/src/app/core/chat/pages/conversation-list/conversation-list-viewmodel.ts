import { inject, Inject } from '@angular/core'

import { UUID } from '~ycyw/shared'

import { ConversationStore } from '@ycyw/support-core/chat/stores'

@Inject({
  providedIn: 'root',
})
export class ConversationListViewModel {
  private readonly store = inject(ConversationStore)

  readonly conversations = this.store.conversations
  readonly loading = this.store.loading

  reloadAll() {
    this.store.reloadAll()
  }

  getFaq(id: UUID) {
    return this.store.getConversation(id)
  }
}
