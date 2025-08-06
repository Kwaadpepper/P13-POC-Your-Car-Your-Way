import { Routes } from '@angular/router'

import { GuestGuard } from './guards'
import { HomePage } from './pages'

export const redirectUrls = {
  home: '',
  posts: '/posts',
  login: '/login',
  register: '/register',
}

export const routes: Routes = [
  {
    path: '',
    canActivate: [GuestGuard],
    title: 'Bienvenue',
    component: HomePage,
  },
  {
    path: 'login',
    canActivate: [],
    title: 'Login',
    component: HomePage,
  },
]
