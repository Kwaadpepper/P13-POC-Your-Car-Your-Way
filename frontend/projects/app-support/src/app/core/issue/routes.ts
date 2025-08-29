import { Routes } from '@angular/router'

import { issueResolver } from './resolvers'

export const issueRoutes: Routes = [
  {
    path: 'issues',
    title: 'Tickets',
    loadComponent: () => import('./pages').then(c => c.IssueList),
  },
  {
    path: 'issues/:id',
    title: () => 'Détail du ticket',
    loadComponent: () => import('./pages').then(c => c.IssuePage),
    resolve: {
      issue: issueResolver,
    },
  },
]
