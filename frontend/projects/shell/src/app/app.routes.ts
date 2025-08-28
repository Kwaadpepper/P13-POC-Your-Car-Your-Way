import { Routes } from '@angular/router'

import { authRoutes } from '@ycyw/shell-core/auth/routes'
import { homeRoutes } from '@ycyw/shell-core/home/routes'

export const routes: Routes = [
  ...authRoutes,
  ...homeRoutes,
  {
    path: '**',
    loadComponent: () => import('@ycyw/shell-core/home/pages').then(c => c.NotFound),
  },
]
