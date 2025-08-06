import { NgClass } from '@angular/common'
import { Component, inject, input, output } from '@angular/core'
import { RouterModule } from '@angular/router'
import { ButtonModule } from 'primeng/button'

import { NavMenuViewModel } from './nav-menu.viewmodel'

@Component({
  selector: 'shell2-nav-menu',
  imports: [
    RouterModule,
    NgClass,
    ButtonModule,
  ],
  providers: [NavMenuViewModel],
  templateUrl: './nav-menu.html',
  styleUrl: './nav-menu.css',
})
export class NavMenu {
  readonly verticalDisplay = input(false, {
    transform: (v: string | boolean) => v !== false,
  })

  readonly closedMenu = output<boolean>()

  readonly viewModel = inject(NavMenuViewModel)

  onCloseMenu(): void {
    this.closedMenu.emit(true)
  }
}
