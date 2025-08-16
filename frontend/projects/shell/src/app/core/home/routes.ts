import { Routes } from '@angular/router'

import { loadRemoteModule } from '@angular-architects/native-federation'

import { AuthClientGuard, AuthOperatorGuard } from '@ycyw/shell-shared/guards'

export const homeRoutes: Routes = [
  {
    path: 'backoffice',
    canActivate: [AuthOperatorGuard],
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'app-backoffice',
        exposedModule: './routes',
      }).then(m => m.routes),
  },
  {
    path: 'reservation',
    canActivate: [AuthClientGuard],
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'app-reservation',
        exposedModule: './routes',
      }).then(m => m.routes),
  },
]
