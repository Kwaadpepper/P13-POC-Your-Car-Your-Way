import { Component, computed, effect, inject, input, OnDestroy, OnInit, signal } from '@angular/core'

import { DividerModule } from 'primeng/divider'
import { ScrollTopModule } from 'primeng/scrolltop'
import { TagModule } from 'primeng/tag'
import { ObservedValueOf } from 'rxjs'

import { UUID } from '@ycyw/shared'
import { PresenceEvent } from '@ycyw/support-domains/events/presence-event'
import { TypingEvent } from '@ycyw/support-domains/events/typing-event'
import { PresenceStatus, Role } from '@ycyw/support-shared/enums'
import { CHAT_SERVICE } from '@ycyw/support-tokens/chat-service-token'

import { MessageInput } from '../message-input/message-input'
import { MessageList } from '../message-list/message-list'
import { TypingIndicator } from '../typing-indicator/typing-indicator'

interface ChatBoxMessage {
  id: UUID
  conversation: UUID
  from: {
    id: UUID
    role: Role
  }
  text: string
  sentAt: Date
}

export interface ChatBoxUser { id: UUID, name: string, isOperator: boolean }
interface ChatBoxParticipant { user: UUID, role: Role, status: PresenceStatus }
interface ChatBoxTypingUser { user: UUID, role: Role }

@Component({
  selector: 'support-chatbox',
  imports: [
    TagModule,
    DividerModule,
    ScrollTopModule,
    MessageList,
    MessageInput,
    TypingIndicator,
  ],
  templateUrl: './chatbox.html',
  styleUrl: './chatbox.css',
})
export class ChatBox implements OnInit, OnDestroy {
  private readonly chatService = inject(CHAT_SERVICE)

  // Inputs
  readonly user = input.required<ChatBoxUser>()
  readonly conversation = input.required<UUID>()

  // State (signals locaux)
  private readonly _connected = signal(false)
  private readonly _conversationId = signal<UUID | null>(null)
  private readonly _messages = signal<ChatBoxMessage[]>([])
  private readonly _participants = signal<ChatBoxParticipant[]>([])
  private readonly _typingUsers = signal<ChatBoxTypingUser[]>([])

  // Sélecteurs exposés
  readonly messages = this._messages.asReadonly()
  readonly participants = this._participants.asReadonly()
  readonly typingUsers = this._typingUsers.asReadonly()
  readonly onlineCount = computed(() =>
    this._participants().filter(p => p.status === PresenceStatus.ONLINE).length,
  )

  // Rejoin si l'Input conversation change après connexion
  private readonly _rejoinEffect = effect(() => {
    const id = this.conversation
    if (!id || !this._connected()) return
    this.join(id())
  })

  ngOnInit() {
    if (!this.user().id) throw new Error('ChatBox: user.id requis')
    if (!this.conversation) throw new Error('ChatBox: conversation requis')

    this.connect()
      .then(() => this.join(this.conversation()))
      .catch(err => console.error('ChatBox: échec de la connexion', err))
  }

  ngOnDestroy(): void {
    this.leave()
    if (this._connected()) this.chatService.disconnect()
  }

  /* Actions UI (reliées aux composants enfants) */
  onSend(message: string) {
    const cid = this._conversationId()
    if (!cid || !message.trim()) return
    this.chatService.sendMessage(cid, message.trim())
    // Arrêt du typing
    this.setTyping(false)
  }

  onTyping(isTyping: boolean) {
    this.setTyping(isTyping)
  }

  /* Connexion + flux */
  private async connect() {
    if (this._connected()) return
    await this.chatService.connect()
    this.bindStreams()
    this._connected.set(true)
  }

  private bindStreams() {
    // Messages individuels
    this.chatService.messages$.subscribe((m) => {
      const cid = this._conversationId()
      if (cid && m.conversation === cid) {
        this._messages.update(arr => [...arr, this.mapToChatBoxMessage(m)])
      }
    })

    // Historique
    this.chatService.history$.subscribe((list) => {
      this._messages.set(list.map(m => this.mapToChatBoxMessage(m)))
    })

    // Présence
    this.chatService.presence$.subscribe((p: PresenceEvent) => {
      const cid = this._conversationId()
      if (p.conversation && cid && p.conversation !== cid) return
      const arr = [...this._participants()]
      const idx = arr.findIndex(x => x.user === p.user)
      const mapped = this.mapToChatBoxParticipant(p)
      if (idx >= 0) arr[idx] = mapped
      else arr.push(mapped)
      this._participants.set(arr)
    })

    // Typing
    this.chatService.typing$.subscribe((t: TypingEvent) => {
      const cid = this._conversationId()
      if (!cid || t.conversation !== cid) return
      const list = [...this._typingUsers()]
      const idx = list.findIndex(x => x.user === t.user)
      if (t.typing) {
        if (idx === -1) list.push(this.mapToChatBoxTypingUser(t))
      }
      else if (idx >= 0) {
        list.splice(idx, 1)
      }
      this._typingUsers.set(list)
    })
  }

  /* Join / Leave */
  private join(conversationId: UUID) {
    const prev = this._conversationId()
    if (prev && prev !== conversationId) {
      this.chatService.leave(prev)
    }
    this._conversationId.set(conversationId)
    this._messages.set([])
    this._typingUsers.set([])
    this._participants.set([])
    this.chatService.join(conversationId)
    this.chatService.getHistory(conversationId, 50)
  }

  private leave() {
    const cid = this._conversationId()
    if (cid) this.chatService.leave(cid)
    this._conversationId.set(null)
    this._messages.set([])
    this._typingUsers.set([])
    this._participants.set([])
  }

  /* Typing */
  private setTyping(isTyping: boolean) {
    const cid = this._conversationId()
    if (!cid) return
    this.chatService.setTyping(cid, isTyping)
  }

  /* Mappers */
  private mapToChatBoxMessage(msg: ObservedValueOf<typeof this.chatService.messages$>): ChatBoxMessage {
    return {
      id: msg.id,
      conversation: msg.conversation,
      from: {
        id: msg.from.id,
        role: msg.from.role,
      },
      text: msg.text,
      sentAt: msg.sentAt,
    }
  }

  private mapToChatBoxParticipant(p: ObservedValueOf<typeof this.chatService.presence$>): ChatBoxParticipant {
    return {
      user: p.user,
      role: p.role,
      status: p.status,
    }
  }

  private mapToChatBoxTypingUser(t: ObservedValueOf<typeof this.chatService.typing$>): ChatBoxTypingUser {
    return {
      user: t.user,
      role: t.role,
    }
  }
}
