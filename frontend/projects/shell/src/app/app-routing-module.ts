import { loadRemoteModule } from '@angular-architects/native-federation'
import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'

const routes: Routes = [
  {
    path: 'users',
    loadComponent: () =>
      loadRemoteModule({
        remoteName: 'users_app',
        exposedModule: './Component',
      }).then(m => m.AppComponent),
  },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
