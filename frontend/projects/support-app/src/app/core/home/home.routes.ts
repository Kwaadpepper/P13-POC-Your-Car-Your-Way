import { Routes } from '@angular/router'

import { TchatDetail, TchatWindow } from './../../domains/tchat/pages'

export const homeRoutes: Routes = [
  {
    path: 'chat',
    component: TchatWindow,
  },
  {
    path: 'chat/:id',
    component: TchatDetail,
  },
]
