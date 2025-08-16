import { InjectionToken } from '@angular/core'

import { AuthServiceImpl } from '@ycyw/shell-core/auth/services'
import { AuthService } from '@ycyw/shell-domains/auth/services'

export const AUTH_SERVICE = new InjectionToken<AuthService>('AuthServiceToken', {
  providedIn: 'root',
  factory: () => new AuthServiceImpl(),
})
