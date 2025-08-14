import { Routes } from '@angular/router'

import { GuestGuard } from '../../shared/guards'

import { Login } from './pages'

export const redirectUrls = {
  guestHomeUrl: '',
  authBackofficeHomeUrl: '/backoffice',
  authReservationHomeUrl: '/reservation',
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
