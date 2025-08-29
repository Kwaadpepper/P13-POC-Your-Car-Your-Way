import { Routes } from '@angular/router'

export const faqsRoutes: Routes = [
  {
    path: 'faqs',
    title: 'FAQ',
    loadComponent: () => import('./pages').then(c => c.FaqList),
  },
]
