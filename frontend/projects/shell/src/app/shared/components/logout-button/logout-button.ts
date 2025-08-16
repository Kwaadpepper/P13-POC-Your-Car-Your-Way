import { Component, inject, input, OnDestroy } from '@angular/core'
import { Router } from '@angular/router'

import { Button, ButtonModule } from 'primeng/button'
import { SubscriptionLike } from 'rxjs'

import { ToastService } from '@ycyw/shell-shared/services'

import { LogoutButtonViewModel } from './logout-button-viewmodel'

@Component({
  selector: 'shell-logout-button',
  imports: [ButtonModule],
  providers: [LogoutButtonViewModel],
  templateUrl: './logout-button.html',
  styleUrl: './logout-button.css',
})
export class LogoutButton implements OnDestroy {
  public redirectUrl = input.required<string>()
  public variant = input<Button['variant']>('outlined')
  public severity = input<Button['severity']>('contrast')
  public title = input<string>()

  private logoutSubscription: SubscriptionLike | null = null

  public readonly viewModel = inject(LogoutButtonViewModel)
  private readonly router = inject(Router)
  private readonly toastService = inject(ToastService)

  ngOnDestroy(): void {
    if (this.logoutSubscription) {
      this.logoutSubscription.unsubscribe()
    }
  }

  public onLogout(): void {
    this.logoutSubscription = this.viewModel.logout()
      .subscribe({
        next: () => {
          this.toastService.info('Vous êtes déconnecté')
          this.router.navigateByUrl(this.redirectUrl())
        },
        error: (error) => {
          console.error('Error:', error)
          this.toastService.error('Erreur lors de la déconnexion')
        },
      })
  }
}
