import { NgClass } from '@angular/common'
import { Component, inject, input, output } from '@angular/core'
import { RouterModule } from '@angular/router'
import { ButtonModule } from 'primeng/button'

import { NavMenuViewModel } from './nav-menu.viewmodel'

@Component({
  selector: 'app-nav-menu',
  imports: [
    RouterModule,
    NgClass, ButtonModule,
  ],
  providers: [NavMenuViewModel],
  templateUrl: './nav-menu.component.html',
  styleUrl: './nav-menu.component.css',
})
export class NavMenuComponent {
  readonly verticalDisplay = input(false, {
    transform: (v: string | boolean) => v !== false,
  })

  readonly closedMenu = output<boolean>()

  readonly viewModel = inject(NavMenuViewModel)

  onCloseMenu(): void {
    this.closedMenu.emit(true)
  }
}
