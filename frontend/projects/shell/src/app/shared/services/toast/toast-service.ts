import { inject, Injectable } from '@angular/core'

import { MessageService } from 'primeng/api'

@Injectable({
  providedIn: 'root',
  deps: [
    MessageService,
  ],
})
export class ToastService {
  private readonly messageService = inject(MessageService)

  public success(message: string, summary?: string): void {
    this.messageService.add({
      severity: 'success',
      summary: summary,
      detail: message,
    })
  }

  public info(message: string, summary?: string): void {
    this.messageService.add({
      severity: 'info',
      summary: summary,
      detail: message,
    })
  }

  public warning(message: string, summary?: string): void {
    this.messageService.add({
      severity: 'warn',
      summary: summary,
      detail: message,
    })
  }

  public error(message: string, summary?: string): void {
    this.messageService.add({
      severity: 'error',
      summary: summary,
      detail: message,
    })
  }
}
