import { inject, Injectable, ErrorHandler as NgErrorHanlder } from '@angular/core'
import { NotFoundError } from '@angular/core/primitives/di'
import { Router } from '@angular/router'

import { SessionExpiredError } from '@ycyw/shared'
import { ToastService } from '@ycyw/shell-shared/services'

/**
* This allows to manage errors in a centralized way
*/
@Injectable({
  providedIn: 'root',
  deps: [
    ToastService,
    Router,
  ],
})
export class ErrorHandler implements NgErrorHanlder {
  private readonly toastService = inject(ToastService)
  private readonly router = inject(Router)

  // NOTE: Forced type any from core/angular
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  handleError(error: any): void {
    if (error instanceof NotFoundError) {
      this.router.navigateByUrl('/404')
      return
    }

    if (error instanceof SessionExpiredError) {
      this.router.navigateByUrl('/login')
      return
    }

    if (error instanceof Error) {
      if (error.cause instanceof SessionExpiredError) {
        this.router.navigateByUrl('/login')
        return
      }
      // ! Unmanaged error occured
      const message = `Sorry an error occured ${error?.message}`
      console.error(message)
      this.toastService.error(message)
    }
    console.debug(error)
  }
}
