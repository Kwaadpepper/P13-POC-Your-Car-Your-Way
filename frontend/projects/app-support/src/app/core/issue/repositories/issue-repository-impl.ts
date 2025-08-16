import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { SessionExpiredError, verifyResponseType } from '@ycyw/shared'
import { IssueListZod } from '@ycyw/support-core/api/schemas'
import issueListSchema from '@ycyw/support-core/api/schemas/issue-list-schema'
import { Issue } from '@ycyw/support-domains/issue/models'
import { IssueRepository } from '@ycyw/support-domains/issue/repositories/issue-repository'
import { environment } from '@ycyw/support-env/environment'

@Injectable({
  providedIn: 'root',
})
export class IssueRepositoryImpl implements IssueRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.endpoint

  readonly resourceUrl = `${this.serviceUrl}/api/support`

  private readonly faqListUrl = `${this.resourceUrl}/issues`

  getAll(): Observable<Issue[]> {
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
