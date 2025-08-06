import { Routes } from '@angular/router'
// NOTE: Cannot use TS aliases here due to NativeFederation limitations
import { homeRoutes } from '@shell-core/home/routes'

import { Dashboard } from './core/home/pages'

export const routes: Routes = [
  {
    path: '',
    component: Dashboard,
  },
  ...homeRoutes,
]
