import { Routes } from '@angular/router'

import { GuestGuard } from './core/auth/guard'
import { HomeComponent } from './pages'

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
    component: HomeComponent,
  },
  {
    path: 'login',
    canActivate: [],
    title: 'Bienvenue',
    component: HomeComponent,
  },
  {
    path: 'posts',
    canActivate: [],
    title: 'Bienvenue',
    component: HomeComponent,
  },
  // {
  //   path: 'users',
  //   loadComponent: () => loadRemoteModule('users-app', './TchatWindow').then(m => m.TchatWindow),
  // },
  // {
  //   path: 'users2',
  //   component: TchatWindow,
  // },
]
