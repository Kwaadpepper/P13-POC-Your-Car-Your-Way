import { inject, Injectable } from '@angular/core'

import { Subject } from 'rxjs'
import { z, ZodError, ZodSchema } from 'zod'

import messageHistorySchema from '~support-core/api/schemas/message-history-schema'
import messageSchema from '~support-core/api/schemas/message-schema'
import presenceEventSchema from '~support-core/api/schemas/presence-event-schema'
import typingEventSchema from '~support-core/api/schemas/typing-event-schema'
import { ChatMessage, ConversationId } from '~support-domains/chat/models'
import { PresenceEvent, TypingEvent } from '~support-domains/events'
import { CHAT_TRANSPORT } from '~support-tokens/chat-transport-token'

import { ChatClient } from '../libs'
import {
  HistoryEventPayload,
  MessageEventPayload,
  PresenceEventPayload,
  TypingEventPayload,
} from '../libs/chat-client'

@Injectable({
  providedIn: 'root',
  deps: [
    CHAT_TRANSPORT,
  ],
})
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
    this.client.onHistory(h => this.historySub.next(this.mapToHistoryChatMessage(h)))
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

  private mapToChatMessage(payload: MessageEventPayload): ChatMessage {
    const parsed = this.validateRequest(messageSchema, payload)

    if (parsed.errors !== null) {
      console.error('Failed to parse message payload:', parsed.errors, payload)
      throw new Error('Invalid message payload')
    }

    return parsed.validated
  }

  private mapToHistoryChatMessage(payload: HistoryEventPayload): ChatMessage[] {
    const parsed = this.validateRequest(messageHistorySchema, payload.messages)

    if (parsed.errors !== null) {
      console.error('Failed to parse history message payload:', parsed.errors, payload)
      throw new Error('Invalid message payload')
    }

    return parsed.validated
  }

  private mapPresenceEvent(payload: PresenceEventPayload): PresenceEvent {
    const parsed = this.validateRequest(presenceEventSchema, payload)

    if (parsed.errors !== null) {
      console.error('Failed to parse presence payload:', parsed.errors, payload)
      throw new Error('Invalid message payload')
    }

    return parsed.validated
  }

  private mapTypingEvent(payload: TypingEventPayload): TypingEvent {
    const parsed = this.validateRequest(typingEventSchema, payload)

    if (parsed.errors !== null) {
      console.error('Failed to parse typing payload:', parsed.errors, payload)
      throw new Error('Invalid message payload')
    }

    return parsed.validated
  }

  private validateRequest<SchemaT extends ZodSchema>(
    schema: SchemaT,
    payload: unknown,
  ): {
    validated: null
    errors: Map<string, string>
  } | {
    validated: z.output<SchemaT>
    errors: null
  } {
    const res = schema.safeParse(payload)
    return res.success === true
      ? { validated: res.data, errors: null }
      : { validated: null, errors: this.zErrorToFieldsError(res.error) }
  }

  private zErrorToFieldsError<SchemaT extends ZodSchema>(
    zError: ZodError<z.infer<SchemaT>>,
  ): Map<string, string> {
    const errors = Object.entries(zError.flatten().formErrors)
      .concat(Object.entries(zError.flatten().fieldErrors))
    const output = new Map<string, string>()

    errors.forEach((entry) => {
      const index = entry[0]
      const errorMessages = entry[1]
      if (errorMessages.length) {
        output.set(index, errorMessages)
      }
    })

    return output
  }
}
