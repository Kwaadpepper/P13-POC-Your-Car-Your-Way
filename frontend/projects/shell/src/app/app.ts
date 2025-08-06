import { Component } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { Header } from '@shell-core/layout'
import { ToastModule } from 'primeng/toast'

@Component({
  selector: 'shell-root',
  templateUrl: './app.html',
  styleUrl: './app.css',
  imports: [
    RouterOutlet,
    Header,
    ToastModule,
  ],
})
export class App {
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
