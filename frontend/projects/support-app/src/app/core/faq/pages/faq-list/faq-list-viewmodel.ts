import { Injectable, inject } from '@angular/core'

import { FaqStore } from '~support-core/faq/stores/faq-store'
import { Faq } from '~support-domains/faq/models'

export interface FaqGroup {
  type: string
  faqs: Faq[]
}

@Injectable()
export class FaqListViewModel {
  private readonly store = inject(FaqStore)

  readonly faqs = this.store.faqs
  readonly loading = this.store.loading

  reloadAll() {
    this.store.reloadAll()
  }

  getFaq(id: string) {
    return this.store.getFaq(id)
  }
}
