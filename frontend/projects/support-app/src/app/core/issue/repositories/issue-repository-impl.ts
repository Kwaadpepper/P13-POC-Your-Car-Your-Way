import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, InjectionToken } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { IssueListZod } from '~support-core/api/schemas'
import issueListSchema from '~support-core/api/schemas/issue-list-schema'
import { Issue } from '~support-domains/issue/models'
import { IssueRepository } from '~support-domains/issue/repositories/issue-repository'
import { environment } from '~support-env/environment'
import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

export const IssueRepositoryInjector = new InjectionToken<IssueRepository>('IssueRepositoryInjector', {
  providedIn: 'root',
  factory: () => new IssueRepositoryImpl(),
})

export class IssueRepositoryImpl implements IssueRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.supportWebServer
  private readonly supportServiceUrl = `${this.serviceUrl}/api/support`
  private readonly faqListUrl = `${this.supportServiceUrl}/issues`

  getIssues(): Observable<Issue[]> {
    return this.http.get<IssueListZod>(
      this.faqListUrl,
      {},
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new SessionExpiredError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(issueListSchema),
    )
  }
}
