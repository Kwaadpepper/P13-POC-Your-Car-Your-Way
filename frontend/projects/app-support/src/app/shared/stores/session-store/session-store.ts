import { DestroyRef, inject, Injectable, signal } from '@angular/core'
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop'

import {
  SESSION_USER_STORAGE_KEY,
  SessionBroadcastMessage,
  SessionBroadcastService,
  SessionBroadcastType,
  SessionSnapshot,
  SessionStoreReadonly,
  SharedUserProfile,
} from '@ycyw/shared'

@Injectable({ providedIn: 'root' })
export class SessionStore implements SessionStoreReadonly {
  private readonly _session = signal<SessionSnapshot>({ isLoggedIn: false, user: null })
  readonly session = this._session.asReadonly()
  readonly session$ = toObservable(this.session)

  private readonly bus = inject(SessionBroadcastService)
  private readonly destroyRef = inject(DestroyRef)

  constructor() {
    this.restoreFromPersistence()

    this.bus.events$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((msg: SessionBroadcastMessage) => {
        switch (msg.type) {
          case SessionBroadcastType.LOGIN:
            this.setUser(msg.payload.user)
            break
          case SessionBroadcastType.LOGOUT:
            this.setUser(null)
            break
          case SessionBroadcastType.REFRESH:
            this.restoreUserOnly()
            break
        }
      })
  }

  snapshot(): SessionSnapshot {
    return this.session()
  }

  private setUser(user: SharedUserProfile | null): void {
    if (this.usersEqual(this._session().user, user)) return
    this._session.set({ isLoggedIn: !!user, user })
  }

  private restoreFromPersistence(): void {
    const user = this.readPersistedUser()
    this._session.set({ isLoggedIn: !!user, user })
  }

  private restoreUserOnly(): void {
    const user = this.readPersistedUser()
    if (this.usersEqual(this._session().user, user)) return
    this._session.set({ isLoggedIn: !!user, user })
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
}
