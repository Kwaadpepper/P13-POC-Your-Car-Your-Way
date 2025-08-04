import { Routes } from '@angular/router'
import { TchatDetail } from './components/tchat-detail/tchat-detail'
import { TchatWindow } from './components/tchat-window/tchat-window'

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
