import { Component } from '@angular/core'
import { RouterModule } from '@angular/router'

import { SessionListener } from '~support-shared/components'

@Component({
  selector: 'support-root',
  imports: [
    RouterModule,
    SessionListener,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
}
