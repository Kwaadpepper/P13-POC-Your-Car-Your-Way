import { Injectable } from '@angular/core'

import { Subject } from 'rxjs'
import { v4 as uuidv4 } from 'uuid'

import {
  LoginEvent,
  LogoutEvent,
  RefreshEvent,
  SESSION_BROADCAST_CHANNEL,
  SessionBroadcastMessage,
  SessionBroadcastType,
  SharedUserProfile,
} from './types'

@Injectable({ providedIn: 'root' })
export class SessionBroadcastService {
  private readonly instanceId = uuidv4()
  private readonly channel = new BroadcastChannel(SESSION_BROADCAST_CHANNEL)

  private currentUser: SharedUserProfile | null = null
  private readonly subject = new Subject<SessionBroadcastMessage>()
  readonly events$ = this.subject.asObservable()

  private static readonly EMPTY: LogoutEvent & RefreshEvent = Object.freeze({})

  get user(): SharedUserProfile | null {
    return this.currentUser
  }

  constructor() {
    this.channel.onmessage = this.onIncoming.bind(this)
  }

  publishLogin(user: SharedUserProfile): void {
    const payload: LoginEvent = { user }
    this.currentUser = user
    this.publish({
      type: SessionBroadcastType.LOGIN,
      payload,
    })
  }

  publishLogout(): void {
    this.currentUser = null
    this.publish({
      type: SessionBroadcastType.LOGOUT,
      payload: SessionBroadcastService.EMPTY,
    })
  }

  publishRefresh(): void {
    this.publish({
      type: SessionBroadcastType.REFRESH,
      payload: SessionBroadcastService.EMPTY,
    })
  }

  private publish(
    partial: Omit<SessionBroadcastMessage, 'sourceId' | 'timestamp'>,
  ): void {
    const message = {
      ...partial,
      sourceId: this.instanceId,
      timestamp: Date.now(),
    }
    this.channel.postMessage(message)
  }

  private onIncoming(event: MessageEvent<SessionBroadcastMessage>): void {
    const msg = event.data
    if (!msg || msg.sourceId === this.instanceId) return

    switch (msg.type) {
      case SessionBroadcastType.LOGIN:
        this.currentUser = msg.payload.user
        break
      case SessionBroadcastType.LOGOUT:
        this.currentUser = null
        break
      case SessionBroadcastType.REFRESH:
        // Pas de changement direct
        break
    }

    this.subject.next(msg)
  }
}
