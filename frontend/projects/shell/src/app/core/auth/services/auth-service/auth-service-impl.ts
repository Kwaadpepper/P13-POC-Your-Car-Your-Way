import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, first, map, Observable, throwError } from 'rxjs'

import { checkServerReponse, verifyResponseType } from '@ycyw/shared'
import { LoginRequest, RegisterRequest } from '@ycyw/shell-core/api/requests'
import { simpleMessageSchema, SimpleMessageZod, userSchema, UserZod } from '@ycyw/shell-core/api/schemas'
import { LoginFailureError } from '@ycyw/shell-core/errors'
import { User } from '@ycyw/shell-domains/auth/models'
import { AuthService } from '@ycyw/shell-domains/auth/services'
import { environment } from '@ycyw/shell-env/environment'
import { SessionStore } from '@ycyw/shell-shared/stores'

@Injectable({
  providedIn: 'root',
  deps: [
    HttpClient,
    SessionStore,
  ],
})
export class AuthServiceImpl implements AuthService {
  readonly resourceUrl = `${environment.endpoint}/api/auth`

  private readonly loginUrl = `${this.resourceUrl}/login`
  private readonly refreshUrl = `${this.resourceUrl}/refresh-token`
  private readonly registerUrl = `${this.resourceUrl}/register`
  private readonly logoutUrl = `${this.resourceUrl}/logout`

  private readonly http = inject(HttpClient)
  private readonly sessionStore = inject(SessionStore)

  /**
   * Login user with login and password and so return the user.
   * Cookies are used to store the session.
   * @param login with login and password.
   * @returns the user logged in.
   */
  public login(login: LoginRequest): Observable<User> {
    return this.http.post<UserZod>(
      this.loginUrl,
      login,
      {
        withCredentials: true,
      },
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          this.sessionStore.setLoggedOut()
          return throwError(() => new LoginFailureError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(userSchema),
      map((user) => {
        this.sessionStore.setLoggedIn(user)
        return user
      }),
      first(),
    )
  }

  /**
   * Refresh the session and so return a simple message.
   * @returns a simple message.
   */
  public refreshSession(): Observable<void> {
    return this.http.post<SimpleMessageZod>(
      this.refreshUrl,
      {},
      {
        withCredentials: true,
      },
    ).pipe(
      verifyResponseType(simpleMessageSchema),
      map(() => {
        this.sessionStore.forceRefresh()
      }),
      first(),
    )
  }

  /**
   * Register user with email, username and password and so return the user.
   * Cookies are used to store the session, User is logged in after registration.
   * @param register with email, username and password.
   * @returns the user registered.
   */
  public register(register: RegisterRequest): Observable<User> {
    return this.http.post<UserZod>(
      this.registerUrl,
      register,
      {
        withCredentials: true,
      },
    ).pipe(
      checkServerReponse(),
      verifyResponseType(userSchema),
      map((user) => {
        this.sessionStore.setLoggedIn(user)
        return user
      }),
      first(),
    )
  }

  /**
   * Logout the user and so return a simple message.
   * Removes the session from the cookies.
   * @returns a simple message.
   */
  public logout(): Observable<void> {
    return this.http.post<SimpleMessageZod>(
      this.logoutUrl,
      {},
      {
        withCredentials: true,
      },
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new LoginFailureError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(simpleMessageSchema),
      map(() => {
        this.sessionStore.setLoggedOut()
      }),
      first(),
    )
  }
}
