import { Component, signal } from '@angular/core'

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  // eslint-disable-next-line @angular-eslint/prefer-standalone
  standalone: false,
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('users-app')
}
