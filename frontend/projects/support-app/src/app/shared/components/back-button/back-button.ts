import { Component, inject, Input } from '@angular/core'
import { Router, RouterLink } from '@angular/router'
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

  private readonly router = inject(Router)
}
