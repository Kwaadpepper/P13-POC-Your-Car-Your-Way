import { Injectable, computed, inject } from '@angular/core'

import { UUID } from '@ycyw/shared'
import { FaqStore } from '@ycyw/support-core/faq/stores'
import { Faq } from '@ycyw/support-domains/faq/models'

@Injectable({
  providedIn: 'root',
  deps: [FaqStore],
})
export class FaqListViewModel {
  private readonly store = inject(FaqStore)

  readonly faqs = computed(() => this.store.faqs())
  readonly loading = computed(() => this.store.loading())

  readonly loadingError = computed(() => this.store.error() !== undefined)

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
