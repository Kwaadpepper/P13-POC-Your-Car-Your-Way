import { Routes } from '@angular/router'
import { HomePage } from '@shell-core/auth/pages/home/home'
import { authRoutes } from '@shell-core/auth/routes'
import { homeRoutes } from '@shell-core/home/routes'

export const routes: Routes = [
  {
    path: '',
    component: HomePage,
  },
  ...authRoutes,
  ...homeRoutes,
]
