import { Injectable, computed, inject } from '@angular/core'

import { FaqStore } from '~support-core/faq/stores'
import { Faq } from '~support-domains/faq/models'
import { UUID } from '~ycyw/shared'

@Injectable({
  providedIn: 'root',
  deps: [FaqStore],
})
export class FaqListViewModel {
  private readonly store = inject(FaqStore)

  readonly faqs = this.store.faqs
  readonly loading = this.store.loading

  readonly availableTypes = computed(() => {
    const set = new Set(this.faqs().map(f => f.category).filter(Boolean))
    return Array.from(set).sort((a, b) => a.localeCompare(b))
  })

  getFaqsFrom(text: string, category: Faq['category'] | null): Faq[] {
    return this.faqs().filter((f) => {
      const okType = category === null
        || f.category === category
      if (!okType) return false
      if (!text) return true
      return f.question.toLowerCase().includes(text)
        || f.answer.toLowerCase().includes(text)
    })
  }

  reloadAll() {
    this.store.reloadAll()
  }

  getFaq(id: UUID) {
    return this.store.getFaq(id)
  }
}
