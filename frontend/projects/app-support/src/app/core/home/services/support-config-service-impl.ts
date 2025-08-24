import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { SessionExpiredError, verifyResponseType } from '@ycyw/shared'
import { supportConfigSchema, SupportConfigZod } from '@ycyw/support-core/api/schemas'
import { SupportConfig } from '@ycyw/support-domains/support/dtos'
import { SupportConfigService } from '@ycyw/support-domains/support/services'
import { environment } from '@ycyw/support-env/environment'

@Injectable({
  providedIn: 'root',
  deps: [HttpClient],
})
export class SupportConfigServiceImpl implements SupportConfigService {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.endpoint

  private readonly resourceUrl = `${this.serviceUrl}`

  private readonly companyInfo = `${this.resourceUrl}/company/info`

  getConfig(): Observable<SupportConfig> {
    return this.http.get<SupportConfigZod>(
      this.companyInfo,
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
