import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import faqListSchema, { FaqListZod } from '~support-core/api/schemas/faq-list-schema'
import { Faq } from '~support-domains/faq/models'
import { FaqRepository } from '~support-domains/faq/repositories'
import { environment } from '~support-env/environment'
import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
})
export class FaqRepositoryImpl implements FaqRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.supportWebServer

  readonly resourceUrl = `${this.serviceUrl}/api/support`

  private readonly faqListUrl = `${this.resourceUrl}/faqs`

  getAll(): Observable<Faq[]> {
    return this.http.get<FaqListZod>(
      this.faqListUrl,
      {},
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new SessionExpiredError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(faqListSchema),
    )
  }
}
