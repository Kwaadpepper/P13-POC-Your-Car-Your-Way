import { Injectable, computed, effect, inject, signal } from '@angular/core'

import { IssueStore } from '~support-core/issue/stores/issue-store'
import { Faq } from '~support-domains/faq/models'
import { Issue, IssueId } from '~support-domains/issue/models'

export interface FaqGroup {
  type: string
  faqs: Faq[]
}

@Injectable({
  providedIn: 'root',
  deps: [IssueStore],
})
export class IssuePageViewModel {
  private readonly store = inject(IssueStore)

  private readonly _issueId = signal<IssueId | null>(null)

  // Issue dérivée depuis le cache du store
  readonly issue = computed<Issue | null>(() => {
    const id = this._issueId()
    if (!id) return null
    return this.store.issues().find(i => i.id === id) ?? null
  })

  // Reuse états globaux du store
  readonly loading = this.store.loading

  constructor() {
    effect(() => {
      const id = this._issueId()
      if (!id) return
      const exists = this.store.issues().some(i => i.id === id)
      if (!exists && !this.store.loading()) {
        this.store.reloadAll()
      }
    })
  }

  setIssueId(id: IssueId | null) {
    this._issueId.set(id)
  }

  reload() {
    this.store.reloadAll()
  }

  async getIssue(id: IssueId): Promise<Issue | null> {
    return await this.store.getFaq(id)
  }
}
