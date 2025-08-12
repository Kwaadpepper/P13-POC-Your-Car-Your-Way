import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, InjectionToken } from '@angular/core'

import { catchError, first, Observable, throwError } from 'rxjs'

import { FaqZod } from '~support-core/api/schemas'
import faqListSchema, { FaqListZod } from '~support-core/api/schemas/faq-list-schema'
import faqSchema from '~support-core/api/schemas/faq-schema'
import { Faq, FaqId } from '~support-domains/faq/models'
import { FaqRepository } from '~support-domains/faq/repositories'
import { environment } from '~support-env/environment'
import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

export const FaqRepositoryInjector = new InjectionToken<FaqRepository>('FaqRepositoryInjector', {
  providedIn: 'root',
  factory: () => new FaqRepositoryImpl(),
})

export class FaqRepositoryImpl implements FaqRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.supportWebServer
  private readonly supportServiceUrl = `${this.serviceUrl}/support`
  private readonly faqListUrl = `${this.supportServiceUrl}/faqs`
  private readonly faqUrl = `${this.supportServiceUrl}/faqs/:id`

  getFaqs(): Observable<Faq[]> {
    return this.http.post<FaqListZod>(
      this.faqListUrl,
      {},
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

  getFaqById(id: FaqId): Observable<Faq | null> {
    return this.http.post<FaqZod>(
      this.faqUrl.replace(':id', id),
      {},
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
      verifyResponseType(faqSchema),
      first(),
    )
  }
}
