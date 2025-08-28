import { Routes } from '@angular/router'

import { loadRemoteModule } from '@angular-architects/native-federation'

import { homeRoutes } from '@ycyw/reservation-core/home/routes'

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard',
  },
  ...homeRoutes,
  {
    path: 'support',
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'app-support',
        exposedModule: './routes',
      }).then(m => m.routes),
  },
]
