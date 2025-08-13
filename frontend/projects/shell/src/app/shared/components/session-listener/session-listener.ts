import { CommonModule } from '@angular/common'
import { Component, computed, inject } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { NavigationEnd, Route, Router, RouterModule } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DialogModule } from 'primeng/dialog'
import { filter, map, startWith } from 'rxjs'

import { AuthGuard } from '~shell-core/auth/guards'
import { SessionStore } from '~shell-core/auth/stores'

@Component({
  selector: 'shell-session-listener',
  imports: [
    RouterModule,
    CommonModule,
    DialogModule,
    ButtonModule,
  ],
  templateUrl: 'session-listener.html',
  styleUrl: 'session-listener.css',
})
export class SessionListener {
  private readonly router = inject(Router)
  private readonly store = inject(SessionStore)

  private readonly routeGuarded = toSignal(
    this.router.events.pipe(
      filter((e): e is NavigationEnd => e instanceof NavigationEnd),
      startWith(null), // calcule une première fois au init
      map(() => {
        let snap = this.router.routerState.snapshot.root
        while (snap.firstChild) snap = snap.firstChild
        const cfg = snap.routeConfig

        if (!cfg) {
          return false
        }

        return this.routeHasAuthGuard(cfg)
      }),
    ),
    { initialValue: false },
  )

  public showLogoutDialog = computed(() => {
    return !this.store.session().isLoggedIn && this.routeGuarded()
  })

  onConfirm(): void {
    // Make sure session is logged out
    this.store.setLoggedOut()
    this.router.navigateByUrl('/')
  }

  private routeHasAuthGuard(route: Route): boolean {
    return [
      ...route.canActivate ?? [],
      ...route.canActivateChild ?? [],
      ...route.canMatch ?? [],
    ].some(guard => guard === AuthGuard)
  }
}
