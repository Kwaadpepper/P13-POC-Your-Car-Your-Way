import { CanActivate } from '@angular/router'

export interface AuthGuard extends CanActivate {
  readonly redirectUrl: string
}
