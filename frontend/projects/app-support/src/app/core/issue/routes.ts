import { Routes } from '@angular/router'

import { issueResolver } from './resolvers'

export const issueRoutes: Routes = [
  {
    path: 'issues',
    loadComponent: () => import('./pages').then(c => c.IssueList),
  },
  {
    path: 'issues/:id',
    loadComponent: () => import('./pages').then(c => c.IssuePage),
    resolve: {
      issue: issueResolver,
    },
  },
]
