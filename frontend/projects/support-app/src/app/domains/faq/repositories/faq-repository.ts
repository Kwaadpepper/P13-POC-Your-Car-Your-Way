import { Observable } from 'rxjs'

import { Faq } from '../models'

export interface FaqRepository {
  getFaqs(): Observable<Faq[]>
}
