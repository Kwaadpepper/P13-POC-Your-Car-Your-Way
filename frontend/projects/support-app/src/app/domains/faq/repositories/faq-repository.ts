import { Observable } from 'rxjs'

import { Faq, FaqId } from '../models'

export interface FaqRepository {
  getFaqs(): Observable<Faq[]>
  getFaqById(id: FaqId): Observable<Faq | null>
}
