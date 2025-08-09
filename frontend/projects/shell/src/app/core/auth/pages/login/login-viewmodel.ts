import { inject, Injectable, signal } from '@angular/core'

import { catchError, EMPTY, finalize, Observable, tap, throwError } from 'rxjs'

import { User } from '~shell-core/auth/models'
import { AuthService } from '~shell-core/auth/services'
import { LoginFailure } from '~shell-core/errors'
import { ToastService } from '~shell-shared/services'

@Injectable({
  providedIn: 'root',
  deps: [AuthService, ToastService],
})
export class LoginViewModel {
  public readonly formErrorMessage = signal('')
  public readonly login = signal('login')
  public readonly password = signal('password')

  public readonly loading = signal(false)

  private readonly authService = inject(AuthService)
  private readonly toastService = inject(ToastService)

  /**
   * Authenticate the user and set the session
   *
   * @return  {Observable<boolean>} Observable that emits true if the user is authenticated
   */
  public logginAndSetSession(login: string, password: string): Observable<User> {
    this.loading.set(true)
    return this.authService.login({ login, password })
      .pipe(
        tap(() => {
          this.loading.set(false)
          this.formErrorMessage.set('')
          this.toastService.success('Connexion réussie')
        }),
        catchError((error) => {
          if (error instanceof LoginFailure) {
            this.formErrorMessage.set('Identifiants incorrects')
            return EMPTY
          }

          this.toastService.error('Erreur lors de la connexion')

          console.error('Error:', error)
          return throwError(() => error)
        }),
        finalize(() => {
          this.loading.set(false)
        }),
      )
  }
}
