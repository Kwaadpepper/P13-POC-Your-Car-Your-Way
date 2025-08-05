import { Routes } from '@angular/router'

import { TchatDetail, TchatWindow } from './components'

export const routes: Routes = [
  {
    path: '',
    component: TchatWindow,
  },
  {
    path: ':id',
    component: TchatDetail,
  },
]
