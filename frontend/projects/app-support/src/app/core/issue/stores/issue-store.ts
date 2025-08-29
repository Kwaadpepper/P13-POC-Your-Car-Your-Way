import { computed, DestroyRef, inject, Injectable, resource } from '@angular/core'
import { takeUntilDestroyed } from '@angular/core/rxjs-interop'

import { firstValueFrom } from 'rxjs'

import { Issue, IssueId } from '@ycyw/support-domains/issue/models'
import { ISSUE_REPOSITORY } from '@ycyw/support-tokens/issue-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [ISSUE_REPOSITORY],
})
export class IssueStore {
  private readonly repository = inject(ISSUE_REPOSITORY)
  private readonly _issues = resource({
    defaultValue: [],
    loader: this.loadIssues.bind(this),
  })

  readonly issues = this._issues.value.asReadonly()
  readonly loading = computed(() => this._issues.isLoading())
  readonly error = computed(() => this._issues.error())

  private readonly destroyRef = inject(DestroyRef)

  async getIssue(id: IssueId): Promise<Issue | null> {
    const faq = this._issues.value().find(f => f.id === id)
      ?? (await this.loadIssues()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._issues.reload()
  }

  private async loadIssues() {
    return await firstValueFrom(
      this.repository.getAll()
        .pipe(takeUntilDestroyed(this.destroyRef)),
    )
  }
}
