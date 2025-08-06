import { Component } from '@angular/core'
import { Main } from '@support-core/layout'

@Component({
  selector: 'support-root',
  imports: [
    Main,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
}
