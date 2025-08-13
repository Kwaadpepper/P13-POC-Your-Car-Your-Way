import { Routes } from '@angular/router'

import { IssueList, IssuePage } from './pages'

export const issueRoutes: Routes = [
  {
    path: 'issues',
    component: IssueList,
  },
  {
    path: 'issues/:id',
    component: IssuePage,
  },
]
