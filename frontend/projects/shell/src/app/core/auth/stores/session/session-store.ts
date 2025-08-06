import { computed, Injectable, signal } from '@angular/core'
import { User } from '@shell-core/auth/models'

@Injectable({
  providedIn: 'root',
})
export class SessionStore {
  private readonly user = signal<User | null>(null)
  private readonly _isLoggedIn = signal<boolean>(false)

  public readonly isLoggedIn = computed(() => this._isLoggedIn())
  public readonly loggedUser = computed(() => this.user())

  constructor() {
    if (this.hasLoggedIsStatusInPersistence()) {
      this.persistLoggedIsStatus()
    }
    else {
      this.removeLoggedInStatus()
    }
  }

  /**
   * Set the logged in user.
   * @param user The user to set as logged in.
   */
  public setLoggedIn(user: User): void {
    this.user.set(user)
    this.persistLoggedIsStatus()
  }

  /**
   * Set the logged out user.
   */
  public setLoggedOut(): void {
    this.user.set(null)
    this.removeLoggedInStatus()
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
}
