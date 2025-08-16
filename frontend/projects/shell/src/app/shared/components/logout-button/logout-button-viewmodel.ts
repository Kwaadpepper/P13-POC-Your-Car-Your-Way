import { inject, Injectable } from '@angular/core'

import { map, Observable } from 'rxjs'

import { AUTH_SERVICE } from '@ycyw/shell-tokens/auth-service-token'

@Injectable({
  providedIn: 'root',
  deps: [
    AUTH_SERVICE,
  ],
})
export class LogoutButtonViewModel {
  private readonly authService = inject(AUTH_SERVICE)

  public logout(): Observable<void> {
    return this.authService.logout().pipe(
      map(() => { return }),
    )
  }
}
