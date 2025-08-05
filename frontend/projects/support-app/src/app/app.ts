import { Component } from '@angular/core'
import { RouterModule } from '@angular/router'

@Component({
  selector: 'app-root',
  imports: [
    RouterModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
}

@Component({
  selector: 'app-root',
  // eslint-disable-next-line @angular-eslint/prefer-standalone
  standalone: false,
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class AppForModule {
}
