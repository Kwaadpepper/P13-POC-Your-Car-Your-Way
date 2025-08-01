import { loadRemoteModule } from '@angular-architects/native-federation'
import { Routes } from '@angular/router'

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      loadRemoteModule({
        remoteName: 'users_app',
        exposedModule: './Component',
      }).then(m => m.AppComponent),
  },
]
