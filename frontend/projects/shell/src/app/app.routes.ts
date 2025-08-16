import { Routes } from '@angular/router'

import { HomePage } from '@ycyw/shell-core/auth/pages/home/home'
import { authRoutes } from '@ycyw/shell-core/auth/routes'
import { homeRoutes } from '@ycyw/shell-core/home/routes'
import { GuestGuard } from '@ycyw/shell-shared/guards'

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
    loadComponent: () => import('@ycyw/shell-core/home/pages').then(c => c.NotFound),
  },
]
