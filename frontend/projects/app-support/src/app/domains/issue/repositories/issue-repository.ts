import { Observable } from 'rxjs'

import { Repository } from '@ycyw/shared'
import { Issue } from '@ycyw/support-domains/issue/models'

export interface IssueRepository extends Repository<Issue> {
  getAll(): Observable<Issue[]>
}
