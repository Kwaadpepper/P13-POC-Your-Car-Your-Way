import { computed, inject, Injectable, OnDestroy, signal } from '@angular/core'

import { Subscription } from 'rxjs'

import { User } from '~shell-core/auth/models'
import { LoginEvent, SessionBroadcastService, SessionBroadcastType, SharedUserProfile } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [
    SessionBroadcastService,
  ],
})
export class SessionStore implements OnDestroy {
  private readonly _user = signal<SharedUserProfile | null>(null)
  private readonly _isLoggedIn = signal<boolean>(false)

  public readonly isLoggedIn = computed(() => this._isLoggedIn())
  public readonly loggedUser = computed(() => this._user())

  private readonly bus = inject(SessionBroadcastService)
  private readonly sub!: Subscription

  constructor() {
    // FIXME: Il manque le User au demarrage.
    if (this.hasLoggedIsStatusInPersistence()) {
      this.persistLoggedIsStatus()
    }
    else {
      this.removeLoggedInStatus()
    }

    this.sub = this.bus.events$.subscribe((message) => {
      switch (message.type) {
        case SessionBroadcastType.LOGIN: {
          const loginEvent = message.payload as LoginEvent
          this._isLoggedIn.set(true)
          this._user.set(loginEvent.user)
          break
        }
        case SessionBroadcastType.LOGOUT: {
          this._user.set(null)
          this._isLoggedIn.set(false)
          break
        }
        case SessionBroadcastType.REFRESH: {
          // TODO: Decider d'en faire quelque chose ou pas.
          break
        }
        default:
          break
      }
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  public setLoggedIn(user: User): void {
    this._user.set(user)
    this.persistLoggedIsStatus()
    this.bus.publishLogin(this.mapToSharedUser(user))
  }

  public setLoggedOut(): void {
    this._user.set(null)
    this.removeLoggedInStatus()
    this.bus.publishLogout()
  }

  private persistLoggedIsStatus(): void {
    this._isLoggedIn.set(true)
    localStorage.setItem('loggedin', '')
  }

  private removeLoggedInStatus(): void {
    this._isLoggedIn.set(false)
    localStorage.removeItem('loggedin')
  }

  private hasLoggedIsStatusInPersistence(): boolean {
    return localStorage.getItem('loggedin') !== null
  }

  private mapToSharedUser(user: User): SharedUserProfile {
    const { id, name, role, email } = user
    return {
      id,
      role,
      name,
      email,
    }
  }
}
