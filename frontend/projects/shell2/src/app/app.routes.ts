import { Routes } from '@angular/router'
import { loadRemoteModule } from '@softarc/native-federation-runtime'
import { HomePage } from './home-page/home-page'

export const routes: Routes = [
  {
    path: '',
    component: HomePage,
  },
  {
    path: 'support',
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'support-app',
        exposedModule: './SupportModule',
      }).then(m => m.SupportModule),
  },
]
