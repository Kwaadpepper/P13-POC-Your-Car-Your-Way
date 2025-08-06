import { Routes } from '@angular/router'

import { home } from './core'

export const routes: Routes = [
  {
    path: '',
    component: home.pages.Dashboard,
  },
  ...home.routes,
]
