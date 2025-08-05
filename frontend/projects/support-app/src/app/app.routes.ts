import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'

import { TchatDetail, TchatWindow } from './components'

const routes: Routes = [
  {
    path: '',
    component: TchatWindow,
  },
  {
    path: ':id',
    component: TchatDetail,
  },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
