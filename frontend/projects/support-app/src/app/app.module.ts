import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core'

import { CommonModule } from '@angular/common'
import { RouterModule } from '@angular/router'
import { AppForModule } from './app'
import { routes } from './app.routes'

@NgModule({
  declarations: [
    AppForModule,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
  ],
  bootstrap: [AppForModule],
})
export class AppModule { }
