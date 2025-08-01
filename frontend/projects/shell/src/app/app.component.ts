import { Component } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { ToastModule } from 'primeng/toast'

import { HeaderComponent } from '@layout'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    RouterOutlet,
    HeaderComponent,
    ToastModule,
  ],
})
export class AppComponent {
  onActivate(): void {
    this.scrollToTop()
  }

  onAttach(): void {
    this.scrollToTop()
  }

  /**
   * Scrolls the window to the top.
   */
  private scrollToTop(): void {
    if (window.scroll) {
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth',
      })
    }
    else {
      window.scrollTo(0, 0) // For Safari
    }
  }
}
