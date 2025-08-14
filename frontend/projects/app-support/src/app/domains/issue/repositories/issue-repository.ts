import { Observable } from 'rxjs'

import { Issue } from '~support-domains/issue/models'
import { Repository } from '~ycyw/shared'

export interface IssueRepository extends Repository<Issue> {
  getAll(): Observable<Issue[]>
}
