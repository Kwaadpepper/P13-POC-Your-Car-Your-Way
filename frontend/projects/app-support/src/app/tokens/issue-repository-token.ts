import { InjectionToken } from '@angular/core'

import { IssueRepositoryImpl } from '@ycyw/support-core/issue/repositories'
import { IssueRepository } from '@ycyw/support-domains/issue/repositories/issue-repository'

export const ISSUE_REPOSITORY = new InjectionToken<IssueRepository>('IssueRepositoryInjector', {
  providedIn: 'root',
  factory: () => new IssueRepositoryImpl(),
})
