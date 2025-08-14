import { Injectable, computed, inject, resource } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Conversation, ConversationId } from '~support-domains/chat/models'
import { CONVERSATION_REPOSITORY } from '~support-tokens/conversation-repository-token'

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

  async getConversation(id: ConversationId): Promise<Conversation | null> {
    const faq = this._conversations.value().find(f => f.id === id)
      ?? (await this.loadConversations()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._conversations.reload()
  }

  private async loadConversations() {
    return await firstValueFrom(this.repository.getAll())
  }
}
