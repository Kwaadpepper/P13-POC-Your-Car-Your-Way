import { CommonModule } from '@angular/common'
import { Component, inject } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router, RouterModule, RouterOutlet } from '@angular/router'

import { ProgressBarModule } from 'primeng/progressbar'
import { ToastModule } from 'primeng/toast'
import { distinctUntilChanged, filter, map } from 'rxjs'

import { Header } from '~shell-core/layout'
import { SessionListener } from '~shell-shared/components'

@Component({
  selector: 'shell-root',
  templateUrl: './app.html',
  styleUrl: './app.css',
  imports: [
    CommonModule,
    RouterModule,
    RouterOutlet,
    Header,
    ToastModule,
    SessionListener,
    ProgressBarModule,
  ],
})
export class App {
  private readonly router = inject(Router)

  readonly isNavigating = toSignal(
    this.router.events.pipe(
      map((event) => {
        if (event instanceof NavigationStart) return true
        if (event instanceof NavigationEnd
          || event instanceof NavigationCancel
          || event instanceof NavigationError) return false
        return undefined
      }),
      filter((v): v is boolean => v !== undefined),
      distinctUntilChanged(),
    ),
    { initialValue: false },
  )

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
