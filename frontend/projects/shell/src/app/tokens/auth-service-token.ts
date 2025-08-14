import { InjectionToken } from '@angular/core'

import { AuthServiceImpl } from '~shell-core/auth/services'
import { AuthService } from '~shell-domains/auth/services'

export const AUTH_SERVICE = new InjectionToken<AuthService>('AuthServiceToken', {
  providedIn: 'root',
  factory: () => new AuthServiceImpl(),
})
