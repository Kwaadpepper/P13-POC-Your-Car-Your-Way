import { Routes } from '@angular/router'

import { GuestGuard } from '~shell-core/auth/guards'
import { HomePage } from '~shell-core/auth/pages/home/home'
import { authRoutes } from '~shell-core/auth/routes'
import { homeRoutes } from '~shell-core/home/routes'

export const routes: Routes = [
  {
    path: '',
    canActivate: [GuestGuard],
    component: HomePage,
  },
  ...authRoutes,
  ...homeRoutes,
]
