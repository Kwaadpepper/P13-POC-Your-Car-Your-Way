import { computed, DestroyRef, inject, Injectable, resource } from '@angular/core'
import { takeUntilDestroyed } from '@angular/core/rxjs-interop'

import { firstValueFrom } from 'rxjs'

import { Conversation, ConversationId } from '@ycyw/support-domains/chat/models'
import { CONVERSATION_REPOSITORY } from '@ycyw/support-tokens/conversation-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [CONVERSATION_REPOSITORY],
})
export class ConversationStore {
  private readonly repository = inject(CONVERSATION_REPOSITORY)
  private readonly _conversations = resource({
    defaultValue: [],
    loader: this.loadConversations.bind(this),
  })

  readonly conversations = this._conversations.value.asReadonly()
  readonly loading = computed(() => this._conversations.isLoading())
  readonly error = computed(() => this._conversations.error())

  private readonly destroyRef = inject(DestroyRef)

  async getConversation(id: ConversationId): Promise<Conversation | null> {
    const faq = this._conversations.value().find(f => f.id === id)
      ?? (await this.loadConversations()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._conversations.reload()
  }

  private async loadConversations() {
    return await firstValueFrom(
      this.repository.getAll()
        .pipe(takeUntilDestroyed(this.destroyRef)),
    )
  }
}
