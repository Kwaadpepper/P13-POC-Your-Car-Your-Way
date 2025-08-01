import { Routes } from '@angular/router'

import { loadRemoteModule } from '@angular-architects/native-federation'
import { GuestGuard } from './guard'
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
    path: 'users',
    loadComponent: () =>
      loadRemoteModule({
        remoteName: 'users_app',
        exposedModule: './Component',
      }).then(m => m.AppComponent),
  },
]
