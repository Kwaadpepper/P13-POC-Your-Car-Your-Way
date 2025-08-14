import { inject, Injectable, signal } from '@angular/core'

import { catchError, EMPTY, finalize, Observable, tap, throwError } from 'rxjs'

import { LoginFailureError } from '~shell-core/errors'
import { User } from '~shell-domains/auth/models'
import { ToastService } from '~shell-shared/services'
import { AUTH_SERVICE } from '~shell-tokens/auth-service-token'

@Injectable({
  providedIn: 'root',
  deps: [
    AUTH_SERVICE,
    ToastService,
  ],
})
export class LoginViewModel {
  public readonly formErrorMessage = signal('')
  public readonly login = signal('login')
  public readonly password = signal('password')

  public readonly loading = signal(false)

  private readonly authService = inject(AUTH_SERVICE)
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
          if (error instanceof LoginFailureError) {
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
