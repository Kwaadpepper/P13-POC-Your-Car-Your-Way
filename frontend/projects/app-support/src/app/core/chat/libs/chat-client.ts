import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable, signal } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { SessionExpiredError, verifyResponseType } from '@ycyw/shared'
import { simpleMessageSchema, SimpleMessageZod } from '@ycyw/support-core/api/schemas'
import { environment } from '@ycyw/support-env/environment'
import { CHAT_TRANSPORT } from '@ycyw/support-tokens/chat-transport-token'

import { Events, EventType, ServerEvent } from './chat/chat-transport'

type Unsub = () => void

export type MessageEventPayload = Events[EventType.MESSAGE]['server']
export type JoinEventPayload = Events[EventType.JOIN]['server']
export type PresenceEventPayload = Events[EventType.PRESENCE]['server']
export type TypingEventPayload = Events[EventType.TYPING]['server']
export type HistoryEventPayload = Events[EventType.HISTORY]['server']

@Injectable({
  providedIn: 'root',
  deps: [CHAT_TRANSPORT],
})
export class ChatClient {
  private readonly http = inject(HttpClient)
  private readonly transport = inject(CHAT_TRANSPORT)

  private readonly pingUrl = `${environment.endpoint}/chat/ping`

  readonly connectionStatus = signal<'connected' | 'disconnected' | 'connecting'>('disconnected')

  constructor() {
    this.transport.onConnectionChange((status) => {
      console.log('Connection status changed:', status ? 'connected' : 'disconnected')
      this.connectionStatus.set(status ? 'connected' : 'disconnected')
    })
  }

  async connect() {
    this.connectionStatus.set('connecting')
    this.pingChatServer().subscribe({
      next: () => {
        console.log('Ping to chat server successful, proceeding to connect...')
        this.transport.connect()
      },
    })
  }

  disconnect() {
    return this.transport.disconnect()
  }

  join(conversation: string) {
    this.transport.send({ type: EventType.JOIN, payload: { conversation } })
  }

  leave(conversation: string) {
    this.transport.send({ type: EventType.LEAVE, payload: { conversation } })
  }

  sendMessage(conversation: string, text: string) {
    this.transport.send({ type: EventType.MESSAGE, payload: { conversation, text } })
  }

  setTyping(conversation: string, isTyping: boolean) {
    this.transport.send({ type: EventType.TYPING, payload: { conversation, isTyping } })
  }

  getHistory(conversation: string, limit = 50) {
    this.transport.send({ type: EventType.HISTORY, payload: { conversation, limit } })
  }

  onMessage(cb: (m: MessageEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.MESSAGE) cb(evt.payload)
    })
  }

  onJoin(cb: (m: JoinEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.JOIN) cb(evt.payload)
    })
  }

  onPresence(cb: (p: PresenceEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.PRESENCE) cb(evt.payload)
    })
  }

  onTyping(cb: (t: TypingEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.TYPING) cb(evt.payload)
    })
  }

  onHistory(cb: (h: HistoryEventPayload) => void): Unsub {
    return this.transport.onEvent((evt: ServerEvent) => {
      if (evt.type === EventType.HISTORY) cb(evt.payload)
    })
  }

  /** This is used to trigger refresh token or logout before attempting cat connexion */
  private pingChatServer(): Observable<SimpleMessageZod> {
    return this.http.get<SimpleMessageZod>(
      this.pingUrl,
      {
        withCredentials: true,
      },
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new SessionExpiredError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(simpleMessageSchema),
    )
  }
}
