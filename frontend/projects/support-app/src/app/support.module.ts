import { CommonModule } from '@angular/common'
import { NgModule } from '@angular/core'
import { RouterModule } from '@angular/router'

import { TchatDetail, TchatWindow } from './components'

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild([
      { path: '', component: TchatWindow },
      { path: ':id', component: TchatDetail },
    ]),
    TchatWindow,
    TchatDetail,
  ],
})
export class SupportModule {}
