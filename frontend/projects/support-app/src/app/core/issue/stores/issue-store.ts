import { Injectable, computed, inject, resource } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Issue, IssueId } from '~support-domains/issue/models'

import { IssueRepositoryInjector } from '../repositories'

@Injectable({
  providedIn: 'root',
  deps: [IssueRepositoryInjector],
})
export class IssueStore {
  private readonly repository = inject(IssueRepositoryInjector)
  private readonly _issues = resource({
    defaultValue: [],
    loader: this.loadIssues.bind(this),
  })

  readonly issues = this._issues.value.asReadonly()
  readonly loading = computed(() => this._issues.isLoading())
  readonly error = computed(() => this._issues.error())

  async getIssue(id: IssueId): Promise<Issue | null> {
    const faq = this._issues.value().find(f => f.id === id)
      ?? (await this.loadIssues()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._issues.reload()
  }

  private async loadIssues() {
    return await firstValueFrom(this.repository.getIssues())
  }
}
