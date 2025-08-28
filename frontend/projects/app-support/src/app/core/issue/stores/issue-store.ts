import { Injectable, inject, signal } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Issue, IssueId } from '@ycyw/support-domains/issue/models'
import { ISSUE_REPOSITORY } from '@ycyw/support-tokens/issue-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [ISSUE_REPOSITORY],
})
export class IssueStore {
  private readonly repository = inject(ISSUE_REPOSITORY)
  private readonly _issues = signal<Issue[]>([])

  readonly issues = this._issues.asReadonly()

  async getIssue(id: IssueId): Promise<Issue | null> {
    const faq = this.issues().find(f => f.id === id)
      ?? (await this.loadIssues()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this.loadIssues()
  }

  private async loadIssues() {
    return await firstValueFrom(this.repository.getAll())
  }
}
