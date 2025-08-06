import { Component } from '@angular/core'

import { layout } from './core'

@Component({
  selector: 'app-root',
  imports: [
    layout.Main,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
}
