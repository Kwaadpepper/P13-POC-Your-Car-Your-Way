import { Routes } from '@angular/router'

import { HomePage } from '~shell-core/auth/pages/home/home'
import { authRoutes } from '~shell-core/auth/routes'
import { homeRoutes } from '~shell-core/home/routes'
import { GuestGuard } from '~shell-shared/guards'

export const routes: Routes = [
  {
    path: '',
    canActivate: [GuestGuard],
    component: HomePage,
  },
  ...authRoutes,
  ...homeRoutes,
  {
    path: '**',
    loadComponent: () => import('~shell-core/home/pages').then(c => c.NotFound),
  },
]
