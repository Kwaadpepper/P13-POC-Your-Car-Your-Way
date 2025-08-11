import { Component, inject } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { BackButton } from '~support-shared/components'
import { APP_CONFIG } from '~support-tokens/config-token'

@Component({
  selector: 'support-dashboard',
  imports: [
    ButtonModule,
    RouterLink,
    BackButton,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  private readonly config = inject(APP_CONFIG)

  get appName(): string {
    return this.config.appName
  }
}
