import { Component, inject, Input } from '@angular/core'
import { Router } from '@angular/router'

import { Button } from 'primeng/button'

@Component({
  selector: 'shell-back-button',
  imports: [Button],
  templateUrl: './back-button.html',
  styleUrl: './back-button.css',
})
export class BackButton {
  @Input({ required: true })
  public backUrl = ''

  private readonly router = inject(Router)

  public goBack(url: string): void {
    this.router.navigateByUrl(url)
  }

  public onGoBackLink(): void {
    this.goBack(this.backUrl)
  }
}
