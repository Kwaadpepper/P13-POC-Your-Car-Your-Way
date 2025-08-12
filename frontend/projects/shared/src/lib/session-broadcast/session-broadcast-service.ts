import { Injectable } from '@angular/core'

import { Subject } from 'rxjs'

import {
  SESSION_BROADCAST_CHANNEL,
  SessionBroadcastEvent,
  SessionBroadcastMessage,
  SessionBroadcastType,
  SharedUserProfile,
} from './types'

@Injectable({ providedIn: 'root' })
export class SessionBroadcastService {
  private readonly instanceId = crypto.randomUUID()
  private readonly channel = new BroadcastChannel(SESSION_BROADCAST_CHANNEL)

  private currentUser: SharedUserProfile | null = null
  private readonly subject = new Subject<SessionBroadcastMessage<SessionBroadcastEvent>>()
  public readonly events$ = this.subject.asObservable()

  get user(): SharedUserProfile | null {
    return this.currentUser
  }

  constructor() {
    this.channel.onmessage = this.onIncoming.bind(this)
  }

  public publishLogin(user: SharedUserProfile): void {
    this.currentUser = user
    this.publish({ type: SessionBroadcastType.LOGIN, payload: user })
  }

  public publishLogout(): void {
    this.currentUser = null
    this.publish({ type: SessionBroadcastType.LOGOUT })
  }

  public publishRefresh(): void {
    this.publish({ type: SessionBroadcastType.REFRESH })
  }

  private publish<T>(partial: Omit<SessionBroadcastMessage<T>, 'sourceId' | 'timestamp'>): void {
    const message: SessionBroadcastMessage<T> = {
      ...partial,
      sourceId: this.instanceId,
      timestamp: Date.now(),
    }

    // * Broadcast the message
    this.channel.postMessage(message)
  }

  private onIncoming<T extends SessionBroadcastEvent>(event: MessageEvent<SessionBroadcastMessage<T>>): void {
    const msg = event.data
    if (!msg || msg.sourceId === this.instanceId) return
    this.subject.next(msg)
  }
}
