import { InjectionToken } from '@angular/core'

import { IssueRepositoryImpl } from '~support-core/issue/repositories'
import { IssueRepository } from '~support-domains/issue/repositories/issue-repository'

export const ISSUE_REPOSITORY = new InjectionToken<IssueRepository>('IssueRepositoryInjector', {
  providedIn: 'root',
  factory: () => new IssueRepositoryImpl(),
})
