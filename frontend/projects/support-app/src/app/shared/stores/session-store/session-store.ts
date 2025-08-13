import { inject, Injectable, OnDestroy, signal } from '@angular/core'
import { toObservable } from '@angular/core/rxjs-interop'

import { Subscription } from 'rxjs'

import {
  SessionBroadcastMessage,
  SessionBroadcastService,
  SessionBroadcastType,
  SharedUserProfile,
} from '~ycyw/shared'

export interface SessionSnapshot {
  isLoggedIn: boolean
  user: SharedUserProfile | null
}

@Injectable({ providedIn: 'root' })
export class MfeSessionStore implements OnDestroy {
  // Source de vérité unique
  private readonly _session = signal<SessionSnapshot>({ isLoggedIn: false, user: null })

  // API publique minimale
  readonly session = this._session.asReadonly()
  readonly session$ = toObservable(this.session)

  // Services
  private readonly bus = inject(SessionBroadcastService)
  private readonly busSub?: Subscription

  // Persistence (mêmes clés que le shell)
  private readonly LS_FLAG_KEY = 'loggedin'
  private readonly LS_USER_KEY = 'session:user'

  constructor() {
    // Etat initial depuis la persistence
    this.bootstrapFromPersistence()

    // Synchronisation inter‑onglets
    this.busSub = this.bus.events$.subscribe((msg: SessionBroadcastMessage) => {
      switch (msg.type) {
        case SessionBroadcastType.LOGIN: {
          this.setUser(msg.payload.user)
          break
        }
        case SessionBroadcastType.LOGOUT: {
          this.setUser(null)
          break
        }
        case SessionBroadcastType.REFRESH: {
          // Le shell met à jour son état local à partir de la persistence.
          // Ici on relit la persistence pour se réaligner.
          this.restoreUserOnly()
          break
        }
      }
    })
  }

  ngOnDestroy(): void {
    this.busSub?.unsubscribe()
  }

  // Impl interne
  private setUser(user: SharedUserProfile | null): void {
    const current = this._session().user
    if (this.usersEqual(current, user)) return
    this._session.set({ isLoggedIn: !!user, user })
  }

  private bootstrapFromPersistence(): void {
    if (!this.hasFlag()) {
      this._session.set({ isLoggedIn: false, user: null })
      return
    }
    const user = this.readPersistedUser()
    this._session.set({ isLoggedIn: !!user, user })
  }

  private restoreUserOnly(): void {
    const user = this.readPersistedUser()
    if (!user && this._session().isLoggedIn) {
      this._session.set({ isLoggedIn: false, user: null })
      return
    }
    if (user && !this.usersEqual(this._session().user, user)) {
      this._session.set({ isLoggedIn: true, user })
    }
  }

  private hasFlag(): boolean {
    return localStorage.getItem(this.LS_FLAG_KEY) !== null
  }

  private readPersistedUser(): SharedUserProfile | null {
    const raw = localStorage.getItem(this.LS_USER_KEY)
    if (!raw) return null
    try {
      return JSON.parse(raw) as SharedUserProfile
    }
    catch {
      return null
    }
  }

  // Helpers
  private usersEqual(a: SharedUserProfile | null, b: SharedUserProfile | null): boolean {
    if (a === b) return true
    if (!a || !b) return false
    return a.id === b.id
      && a.role === b.role
      && a.name === b.name
      && a.email === b.email
  }
}
