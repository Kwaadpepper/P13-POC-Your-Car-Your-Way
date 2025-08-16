import { Component, inject, input, output } from '@angular/core'
import { RouterModule } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { redirectUrls } from '@ycyw/shell-core/auth/routes'
import { LogoutButton } from '@ycyw/shell-shared/components'

import { NavMenuViewModel } from './nav-menu.viewmodel'

@Component({
  selector: 'shell-nav-menu',
  imports: [
    RouterModule,
    ButtonModule,
    LogoutButton,
  ],
  providers: [NavMenuViewModel],
  templateUrl: './nav-menu.html',
  styleUrl: './nav-menu.css',
})
export class NavMenu {
  readonly verticalDisplay = input(false, {
    transform: (v: string | boolean) => v !== false,
  })

  readonly logoutUrl = redirectUrls.guestHomeUrl
  readonly closedMenu = output<boolean>()

  readonly viewModel = inject(NavMenuViewModel)

  readonly dashboardUrl = this.viewModel.dashboardUrl()
  readonly supportUrl = this.viewModel.supportUrl()

  onCloseMenu(): void {
    this.closedMenu.emit(true)
  }
}
