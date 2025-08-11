import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, first, map, Observable, throwError } from 'rxjs'

import { LoginRequest, RegisterRequest } from '~shell-core/auth/api/requests'
import { simpleMessageSchema, SimpleMessageZod, userSchema, UserZod } from '~shell-core/auth/api/schemas'
import { User } from '~shell-core/auth/models'
import { SessionStore } from '~shell-core/auth/stores'
import { LoginFailure } from '~shell-core/errors'
import { checkServerReponse, verifyResponseType } from '~shell-core/tools'
import { environment } from '~shell-env/environment'

@Injectable({
  providedIn: 'root',
  deps: [
    HttpClient,
    SessionStore,
  ],
})
export class AuthService {
  private readonly endpointUrl = environment.endpoint
  private readonly loginUrl = `${this.endpointUrl}/api/auth/login`
  private readonly refreshUrl = `${this.endpointUrl}/api/auth/refresh-token`
  private readonly registerUrl = `${this.endpointUrl}/api/auth/register`
  private readonly logoutUrl = `${this.endpointUrl}/api/auth/logout`

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
          return throwError(() => new LoginFailure())
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
  public refreshSession(): Observable<SimpleMessageZod> {
    return this.http.post<SimpleMessageZod>(
      this.refreshUrl,
      {},
      {
        withCredentials: true,
      },
    ).pipe(
      verifyResponseType(simpleMessageSchema),
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
  public logout(): Observable<SimpleMessageZod> {
    return this.http.post<SimpleMessageZod>(
      this.logoutUrl,
      {},
      {
        withCredentials: true,
      },
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new LoginFailure())
        }
        return throwError(() => error)
      }),
      verifyResponseType(simpleMessageSchema),
      map((response) => {
        this.sessionStore.setLoggedOut()
        return response
      }),
      first(),
    )
  }
}
