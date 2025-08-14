import { Observable } from 'rxjs'

import { LoginRequest, RegisterRequest } from '~shell-core/api/requests'
import { User } from '~shell-domains/auth/models'

export interface AuthService {
  readonly resourceUrl: string

  login(login: LoginRequest): Observable<User>

  refreshSession(): Observable<void>

  register(register: RegisterRequest): Observable<User>

  logout(): Observable<void>
}
