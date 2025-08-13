import { NgClass } from '@angular/common'
import { Component, inject, input, output } from '@angular/core'
import { RouterModule } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { redirectUrls } from '~shell-core/auth/routes'
import { LogoutButton } from '~shell-shared/components'

import { NavMenuViewModel } from './nav-menu.viewmodel'

@Component({
  selector: 'shell-nav-menu',
  imports: [
    RouterModule,
    NgClass,
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

  onCloseMenu(): void {
    this.closedMenu.emit(true)
  }
}
