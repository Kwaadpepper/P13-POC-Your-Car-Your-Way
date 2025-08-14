import { Observable } from 'rxjs'

import { Repository } from '~ycyw/shared'

import { Faq } from '../models'

export interface FaqRepository extends Repository<Faq> {
  getAll(): Observable<Faq[]>
}
