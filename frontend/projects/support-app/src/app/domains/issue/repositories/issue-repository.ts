import { Observable } from 'rxjs'

import { Issue } from '~support-domains/issue/models'

export interface IssueRepository {
  getIssues(): Observable<Issue[]>
}
