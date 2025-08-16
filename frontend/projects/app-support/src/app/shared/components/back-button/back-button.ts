import { Component, input } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'

@Component({
  selector: 'support-back-button',
  imports: [
    ButtonModule,
    RouterLink,
  ],
  templateUrl: './back-button.html',
  styleUrl: './back-button.css',
})
export class BackButton {
  readonly backUrl = input.required<string>()
  readonly label = input<string>('')
}
