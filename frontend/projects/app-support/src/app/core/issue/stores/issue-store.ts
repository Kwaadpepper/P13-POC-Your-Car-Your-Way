import { computed, inject, Injectable, OnDestroy, resource } from '@angular/core'

import { firstValueFrom, Subscription } from 'rxjs'

import { Issue, IssueId } from '@ycyw/support-domains/issue/models'
import { ISSUE_REPOSITORY } from '@ycyw/support-tokens/issue-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [ISSUE_REPOSITORY],
})
export class IssueStore implements OnDestroy {
  private readonly repository = inject(ISSUE_REPOSITORY)
  private readonly _issues = resource({
    defaultValue: [],
    loader: this.loadIssues.bind(this),
  })

  readonly issues = this._issues.value.asReadonly()
  readonly loading = computed(() => this._issues.isLoading())
  readonly error = computed(() => this._issues.error())

  private readonly sub: Subscription

  constructor() {
    this.sub = this.repository.getAll().subscribe({
      next: issues => this._issues.set(issues),
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  async getIssue(id: IssueId): Promise<Issue | null> {
    const faq = this._issues.value().find(f => f.id === id)
      ?? (await this.loadIssues()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._issues.reload()
  }

  private async loadIssues() {
    return await firstValueFrom(this.repository.getAll())
  }
}
