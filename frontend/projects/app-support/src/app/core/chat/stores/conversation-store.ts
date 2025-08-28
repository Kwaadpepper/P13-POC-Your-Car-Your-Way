import { Injectable, inject, signal } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Conversation, ConversationId } from '@ycyw/support-domains/chat/models'
import { CONVERSATION_REPOSITORY } from '@ycyw/support-tokens/conversation-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [CONVERSATION_REPOSITORY],
})
export class ConversationStore {
  private readonly repository = inject(CONVERSATION_REPOSITORY)
  private readonly _conversations = signal<Conversation[]>([])

  readonly conversations = this._conversations.asReadonly()

  async getConversation(id: ConversationId): Promise<Conversation | null> {
    const faq = this._conversations().find(f => f.id === id)
      ?? (await this.loadConversations()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this.loadConversations()
  }

  private async loadConversations() {
    return await firstValueFrom(this.repository.getAll())
  }
}
