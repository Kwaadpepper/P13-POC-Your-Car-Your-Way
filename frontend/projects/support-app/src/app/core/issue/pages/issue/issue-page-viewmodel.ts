import { Injectable } from '@angular/core'

import { Faq } from '~support-domains/faq/models'

export interface FaqGroup {
  type: string
  faqs: Faq[]
}

@Injectable({
  providedIn: 'root',
})
export class IssuePageViewModel {
}
