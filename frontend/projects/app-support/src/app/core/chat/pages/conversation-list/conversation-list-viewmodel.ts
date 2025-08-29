import { computed, inject, Inject } from '@angular/core'

import { UUID } from '@ycyw/shared'
import { ConversationStore } from '@ycyw/support-core/chat/stores'

@Inject({
  providedIn: 'root',
})
export class ConversationListViewModel {
  private readonly store = inject(ConversationStore)

  readonly conversations = computed(() => this.store.conversations())
  readonly loading = computed(() => this.store.loading())
  readonly loadingError = computed(() => this.store.error() !== undefined)

  reloadAll() {
    this.store.reloadAll()
  }

  getFaq(id: UUID) {
    return this.store.getConversation(id)
  }
}
