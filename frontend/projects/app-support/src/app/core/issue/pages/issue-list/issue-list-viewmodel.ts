import { inject, Inject } from '@angular/core'

import { UUID } from '@ycyw/shared'
import { IssueStore } from '@ycyw/support-core/issue/stores/issue-store'

@Inject({
  providedIn: 'root',
})
export class IssueListViewModel {
  private readonly store = inject(IssueStore)

  readonly issues = this.store.issues
  readonly loading = this.store.loading

  reloadAll() {
    this.store.reloadAll()
  }

  getFaq(id: UUID) {
    return this.store.getIssue(id)
  }
}
