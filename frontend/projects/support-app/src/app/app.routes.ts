import { Routes } from '@angular/router'
import { Dashboard } from '@support-core/home/pages'
import { homeRoutes } from '@support-core/home/routes'

export const routes: Routes = [
  {
    path: '',
    component: Dashboard,
  },
  ...homeRoutes,
]
