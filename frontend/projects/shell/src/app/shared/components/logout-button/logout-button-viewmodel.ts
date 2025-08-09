import { inject, Injectable } from '@angular/core'

import { map, Observable } from 'rxjs'

import { AuthService } from '~shell-core/auth/services'

@Injectable({
  providedIn: 'root',
  deps: [AuthService],
})
export class LogoutButtonViewModel {
  private readonly authService = inject(AuthService)

  public logout(): Observable<void> {
    return this.authService.logout().pipe(
      map(() => { return }),
    )
  }
}
