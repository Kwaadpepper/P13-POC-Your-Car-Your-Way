import { inject, Injectable } from '@angular/core'

import { Subject } from 'rxjs'

import messageSchema from '~support-core/api/schemas/message-schema'
import presenceEventSchema from '~support-core/api/schemas/presence-event-schema'
import typingEventSchema from '~support-core/api/schemas/typing-event-schema'
import { ChatMessage, ConversationId } from '~support-domains/chat/models'
import { PresenceEvent } from '~support-domains/events/presence-event'
import { TypingEvent } from '~support-domains/events/typing-event'

import { ChatClient, Message } from '../libs'
import { PresenceEvent as ChatPresenceEvent, TypingEvent as ChatTypingEvent } from '../libs/chat/events'
import { CHAT_TRANSPORT } from '../tokens'

@Injectable({ providedIn: 'root' })
export class ChatService {
  private readonly client: ChatClient

  private readonly messagesSub = new Subject<ChatMessage>()
  private readonly presenceSub = new Subject<PresenceEvent>()
  private readonly typingSub = new Subject<TypingEvent>()
  private readonly historySub = new Subject<ChatMessage[]>()

  readonly messages$ = this.messagesSub.asObservable()
  readonly presence$ = this.presenceSub.asObservable()
  readonly typing$ = this.typingSub.asObservable()
  readonly history$ = this.historySub.asObservable()

  constructor() {
    const transport = inject(CHAT_TRANSPORT)
    this.client = new ChatClient(transport)

    this.client.onMessage(m => this.messagesSub.next(this.mapToChatMessage(m)))
    this.client.onPresence(p => this.presenceSub.next(this.mapPresenceEvent(p)))
    this.client.onTyping(t => this.typingSub.next(this.mapTypingEvent(t)))
    this.client.onHistory(h => this.historySub.next(h.map(this.mapToChatMessage)))
  }

  connect(token?: string) {
    return this.client.connect(token)
  }

  disconnect() {
    return this.client.disconnect()
  }

  join(conversationId: ConversationId) {
    this.client.join(conversationId)
    // Optionnel: charger l'historique dès le join
    this.client.getHistory(conversationId, 50)
  }

  leave(conversationId: ConversationId) {
    this.client.leave(conversationId)
  }

  sendMessage(conversationId: ConversationId, text: string) {
    this.client.sendMessage(conversationId, text)
  }

  setTyping(conversationId: ConversationId, isTyping: boolean) {
    this.client.setTyping(conversationId, isTyping)
  }

  getHistory(conversationId: ConversationId, limit = 50) {
    this.client.getHistory(conversationId, limit)
  }

  private mapToChatMessage(msg: Message): ChatMessage {
    return messageSchema.parse(msg)
  }

  private mapPresenceEvent(event: ChatPresenceEvent): PresenceEvent {
    return presenceEventSchema.parse(event)
  }

  private mapTypingEvent(event: ChatTypingEvent): TypingEvent {
    return typingEventSchema.parse(event)
  }
}
