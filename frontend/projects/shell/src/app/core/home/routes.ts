import { Routes } from '@angular/router'

import { loadRemoteModule } from '@angular-architects/native-federation'

import { AuthGuard } from '~shell-core/auth/guards'

import { Dashboard } from './pages'

export const homeRoutes: Routes = [
  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    component: Dashboard,
  },
  {
    path: 'support',
    canActivate: [AuthGuard],
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'support-app',
        exposedModule: './routes',
      }).then(m => m.routes),
  },
]
