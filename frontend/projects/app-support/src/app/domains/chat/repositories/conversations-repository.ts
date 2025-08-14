import { Observable } from 'rxjs'

import { Repository } from '~ycyw/shared'

import { Conversation } from '../models'

export interface ConversationRepository extends Repository<Conversation> {
  getAll(): Observable<Conversation[]>
}
