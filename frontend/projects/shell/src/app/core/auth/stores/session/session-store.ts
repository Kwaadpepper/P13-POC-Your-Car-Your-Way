import { inject, Injectable, OnDestroy, signal } from '@angular/core'
import { toObservable } from '@angular/core/rxjs-interop'

import { Subscription } from 'rxjs'

import { User } from '~shell-core/auth/models'
import {
  SESSION_USER_STORAGE_KEY,
  SessionBroadcastMessage,
  SessionBroadcastService,
  SessionBroadcastType,
  SharedUserProfile,
} from '~ycyw/shared'

export interface SessionSnapshot {
  isLoggedIn: boolean
  user: SharedUserProfile | null
}

@Injectable({ providedIn: 'root', deps: [SessionBroadcastService] })
export class SessionStore implements OnDestroy {
  private readonly _session = signal<SessionSnapshot>({ isLoggedIn: false, user: null })
  readonly session = this._session.asReadonly()
  readonly session$ = toObservable(this.session)

  private readonly bus = inject(SessionBroadcastService)
  private readonly busSub?: Subscription

  constructor() {
    this.restoreFromPersistence()

    this.busSub = this.bus.events$.subscribe((msg: SessionBroadcastMessage) => {
      switch (msg.type) {
        case SessionBroadcastType.LOGIN:
          this.setSessionInternal(msg.payload.user, { persist: true })
          break
        case SessionBroadcastType.LOGOUT:
          this.setSessionInternal(null, { persist: true })
          break
        case SessionBroadcastType.REFRESH:
          this.restoreUserOnly()
          break
      }
    })
  }

  setLoggedIn(user: User): void {
    const profile = this.mapToSharedUser(user)
    this.setSessionInternal(profile, { persist: true, broadcast: true })
  }

  setLoggedOut(): void {
    this.setSessionInternal(null, { persist: true, broadcast: true })
  }

  forceRefresh(): void {
    this.bus.publishRefresh()
    this.restoreUserOnly()
  }

  snapshot(): SessionSnapshot {
    return this.session()
  }

  ngOnDestroy(): void {
    this.busSub?.unsubscribe()
  }

  private setSessionInternal(
    user: SharedUserProfile | null,
    options: { persist?: boolean, broadcast?: boolean } = {},
  ): void {
    const { persist, broadcast } = options
    const current = this._session().user
    if (this.usersEqual(current, user)) return

    this._session.set({ isLoggedIn: !!user, user })

    if (persist) {
      if (user) this.persistUser(user)
      else this.clearPersistence()
    }

    if (broadcast) {
      if (user) this.bus.publishLogin(user)
      else this.bus.publishLogout()
    }
  }

  private persistUser(user: SharedUserProfile) {
    try {
      localStorage.setItem(SESSION_USER_STORAGE_KEY, JSON.stringify(user))
    }
    catch { /* noop */ }
  }

  private clearPersistence() {
    localStorage.removeItem(SESSION_USER_STORAGE_KEY)
  }

  private restoreFromPersistence() {
    const user = this.readPersistedUser()
    this._session.set({ isLoggedIn: !!user, user })
  }

  private restoreUserOnly() {
    const user = this.readPersistedUser()
    if (!user && this._session().isLoggedIn) {
      this._session.set({ isLoggedIn: false, user: null })
      return
    }
    if (user && !this.usersEqual(this._session().user, user)) {
      this._session.set({ isLoggedIn: true, user })
    }
  }

  private readPersistedUser(): SharedUserProfile | null {
    const raw = localStorage.getItem(SESSION_USER_STORAGE_KEY)
    if (!raw) return null
    try {
      return JSON.parse(raw) as SharedUserProfile
    }
    catch {
      return null
    }
  }

  private usersEqual(a: SharedUserProfile | null, b: SharedUserProfile | null): boolean {
    if (a === b) return true
    if (!a || !b) return false
    return a.id === b.id
  }

  private mapToSharedUser(user: User): SharedUserProfile {
    const { id, name, role, email } = user
    return { id, name, role, email }
  }
}
