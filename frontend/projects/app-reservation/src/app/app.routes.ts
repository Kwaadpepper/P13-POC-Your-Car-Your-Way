import { Routes } from '@angular/router'

import { loadRemoteModule } from '@angular-architects/native-federation'

import { homeRoutes } from '~reservation-core/home/routes'

export const routes: Routes = [
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
