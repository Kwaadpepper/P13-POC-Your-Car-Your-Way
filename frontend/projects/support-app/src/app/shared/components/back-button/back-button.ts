import { Component, Input } from '@angular/core'
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
  @Input({ required: true })
  public backUrl = ''

  @Input({ required: false })
  public label = ''
}
