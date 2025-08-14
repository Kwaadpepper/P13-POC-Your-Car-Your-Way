import { Routes } from '@angular/router'

export const faqsRoutes: Routes = [
  {
    path: 'faqs',
    loadComponent: () => import('./pages').then(c => c.FaqList),
  },
]
