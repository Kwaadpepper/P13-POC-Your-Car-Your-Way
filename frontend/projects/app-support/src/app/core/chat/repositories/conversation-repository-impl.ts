import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { inject, Injectable } from '@angular/core'

import { catchError, Observable, throwError } from 'rxjs'

import { SessionExpiredError, verifyResponseType } from '~ycyw/shared'

import { ConversationListZod } from '@ycyw/support-core/api/schemas'
import conversationListSchema from '@ycyw/support-core/api/schemas/conversation-list-schema'
import { Conversation } from '@ycyw/support-domains/chat/models'
import { ConversationRepository } from '@ycyw/support-domains/chat/repositories'
import { environment } from '@ycyw/support-env/environment'

@Injectable({
  providedIn: 'root',
})
export class ConversationRepositoryImpl implements ConversationRepository {
  private readonly http = inject(HttpClient)
  private readonly serviceUrl = environment.endpoint

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
