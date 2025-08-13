import { Injectable, computed, inject, signal } from '@angular/core'

import { Subscription } from 'rxjs'

import { ChatService } from '~support-core/chat/services'
import { PresenceStatus, Role } from '~support-domains/chat/enums'
import { ChatMessage, ConversationId, UserId } from '~support-domains/chat/models'
import { PresenceEvent } from '~support-domains/events/presence-event'
import { TypingEvent } from '~support-domains/events/typing-event'
import { LoginEvent, SessionBroadcastService, SessionBroadcastType } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [
    ChatService,
    SessionBroadcastService,
  ],
})
export class ConversationViewModel {
  private readonly chat = inject(ChatService)
  private readonly sessionBus = inject(SessionBroadcastService)

  private readonly subs = new Subscription()
  private connected = false

  private readonly _currentUserId = signal<UserId | null>(this.sessionBus.user?.id ?? null)
  private readonly _conversationId = signal<ConversationId | null>(null)
  private readonly _messages = signal<ChatMessage[]>([])
  private readonly _participants = signal<{ user: UserId, role: Role, status: PresenceStatus }[]>([])
  private readonly _typingUsers = signal<{ user: UserId, role: Role }[]>([])

  // Selectors (signals)
  readonly currentUserId = this._currentUserId.asReadonly()
  readonly conversationId = this._conversationId.asReadonly()
  readonly messages = this._messages.asReadonly()
  readonly participants = this._participants.asReadonly()
  readonly typingUsers = this._typingUsers.asReadonly()

  // Dérivés utiles pour l'UI
  readonly isEmpty = computed(() => this._messages().length === 0)
  readonly onlineCount = computed(() => this._participants().filter(p => p.status === 'online').length)

  async init() {
    if (this.connected) {
      return
    }

    this._currentUserId.set(this.sessionBus.user?.id ?? null)
    if (!this._currentUserId()) {
      throw new Error('User ID is not set. Ensure the user is logged in before initializing the conversation view model.')
    }

    await this.chat.connect()
    this.bindStreams()
    this.connected = true
  }

  // Abonnement aux flux du ChatService
  private bindStreams() {
    this.subs.add(
      this.chat.messages$.subscribe((m) => {
        const cid = this._conversationId()
        if (cid && m.conversation === cid) {
          this._messages.update(arr => [...arr, m])
        }
      }),
    )

    this.subs.add(
      this.chat.history$.subscribe((list) => {
        this._messages.set(list)
      }),
    )

    this.subs.add(
      this.chat.presence$.subscribe((p: PresenceEvent) => {
        const cid = this._conversationId()
        // Si l'event concerne une autre conversation, on ignore
        if (p.conversation && cid && p.conversation !== cid) return

        const arr = [...this._participants()]
        const idx = arr.findIndex(x => x.user === p.user)
        if (idx >= 0) {
          arr[idx] = { user: p.user, role: p.role, status: p.status }
        }
        else {
          arr.push({ user: p.user, role: p.role, status: p.status })
        }
        this._participants.set(arr)
      }),
    )

    this.subs.add(
      this.chat.typing$.subscribe((t: TypingEvent) => {
        const cid = this._conversationId()
        if (!cid || t.conversation !== cid) return

        const list = [...this._typingUsers()]
        const idx = list.findIndex(x => x.user === t.user)
        if (t.typing) {
          if (idx === -1) list.push({ user: t.user, role: t.role })
        }
        else if (idx >= 0) {
          list.splice(idx, 1)
        }
        this._typingUsers.set(list)
      }),
    )

    this.subs.add(
      this.sessionBus.events$.subscribe((message) => {
        if (message.type === SessionBroadcastType.LOGIN) {
          const payload = message.payload as LoginEvent
          this._currentUserId.set(payload.user.id)
        }
        if (message.type === SessionBroadcastType.LOGOUT) {
          this._currentUserId.set(null)
          this.leave()
        }
      }),
    )
  }

  // Navigation/gestion de conversation
  join(conversationId: ConversationId) {
    const prev = this._conversationId()
    if (prev && prev !== conversationId) this.chat.leave(prev)

    this._conversationId.set(conversationId)
    // Reset état local
    this._messages.set([])
    this._typingUsers.set([])
    this._participants.set([])

    this.chat.join(conversationId)
    this.chat.getHistory(conversationId, 50)
  }

  leave() {
    const id = this._conversationId()
    if (id) this.chat.leave(id)
    this._conversationId.set(null)
    this._messages.set([])
    this._typingUsers.set([])
    this._participants.set([])
  }

  // Actions utilisateur
  sendMessage(text: string) {
    const id = this._conversationId()
    if (!id || !text.trim()) return
    this.chat.sendMessage(id, text.trim())
  }

  setTyping(isTyping: boolean) {
    const id = this._conversationId()
    if (!id) return
    this.chat.setTyping(id, isTyping)
  }

  // Nettoyage
  destroy() {
    this.subs.unsubscribe()
  }
}
