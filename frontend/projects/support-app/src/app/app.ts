import { Component } from '@angular/core'
import { RouterModule } from '@angular/router'
import { BackButton } from '@support-shared/components'

@Component({
  selector: 'support-root',
  imports: [
    RouterModule,
    BackButton,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
}
