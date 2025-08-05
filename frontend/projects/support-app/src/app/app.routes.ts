import { Routes } from '@angular/router'

import { homeRoutes } from './core/home/home.routes'
import { Dashboard } from './core/home/pages'

export const routes: Routes = [
  {
    path: '',
    component: Dashboard,
  },
  ...homeRoutes,
]
