import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { ConversationListZod } from '~support-core/api/schemas'
import conversationListSchema from '~support-core/api/schemas/conversation-list-schema'
import { Conversation } from '~support-domains/chat/models'
import { ConversationRepository } from '~support-domains/chat/repositories'
import { environment } from '~support-env/environment'
import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
})
export class ConversationRepositoryImpl implements ConversationRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.supportWebServer

  readonly resourceUrl = `${this.serviceUrl}/api/support`

  private readonly conversationListUrl = `${this.resourceUrl}/conversations`

  getAll(): Observable<Conversation[]> {
    return this.http.get<ConversationListZod>(
      this.conversationListUrl,
      {},
    ).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return throwError(() => new SessionExpiredError())
        }
        return throwError(() => error)
      }),
      verifyResponseType(conversationListSchema),
    )
  }
}
