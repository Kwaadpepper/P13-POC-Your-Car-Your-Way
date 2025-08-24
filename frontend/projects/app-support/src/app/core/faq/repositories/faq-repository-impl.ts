import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { SessionExpiredError, verifyResponseType } from '@ycyw/shared'
import faqListSchema, { FaqListZod } from '@ycyw/support-core/api/schemas/faq-list-schema'
import { Faq } from '@ycyw/support-domains/faq/models'
import { FaqRepository } from '@ycyw/support-domains/faq/repositories'
import { environment } from '@ycyw/support-env/environment'

@Injectable({
  providedIn: 'root',
})
export class FaqRepositoryImpl implements FaqRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.endpoint

  readonly resourceUrl = `${this.serviceUrl}`

  private readonly faqListUrl = `${this.resourceUrl}/faqs`

  getAll(): Observable<Faq[]> {
    return this.http.get<FaqListZod>(
      this.faqListUrl,
      {
        withCredentials: true,
      },
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
