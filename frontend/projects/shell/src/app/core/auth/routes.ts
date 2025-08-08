import { Routes } from '@angular/router'

import { GuestGuard } from './guards'
import { Login } from './pages'

export const redirectUrls = {
  guestHomeUrl: '',
  authHomeUrl: '/dashboard',
  login: '/login',
  register: '/register',
}

export const authRoutes: Routes = [
  {
    path: 'login',
    canActivate: [GuestGuard],
    title: 'Login',
    component: Login,
  },
]
