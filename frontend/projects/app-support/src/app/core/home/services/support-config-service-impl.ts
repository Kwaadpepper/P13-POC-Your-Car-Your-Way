import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { supportConfigSchema, SupportConfigZod } from '~support-core/api/schemas'
import { SupportConfig } from '~support-domains/support/dtos'
import { SupportConfigService } from '~support-domains/support/services'
import { environment } from '~support-env/environment'
import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [HttpClient],
})
export class SupportConfigServiceImpl implements SupportConfigService {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.endpoint

  private readonly resourceUrl = `${this.serviceUrl}/api/support`

  getConfig(): Observable<SupportConfig> {
    return this.http.get<SupportConfigZod>(
      this.resourceUrl,
      {},
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new SessionExpiredError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(supportConfigSchema),
    )
  }
}
