import { Observable } from 'rxjs'

import { Conversation } from '../models'

export interface ConversationRepository {
  getConversations(): Observable<Conversation[]>
}
