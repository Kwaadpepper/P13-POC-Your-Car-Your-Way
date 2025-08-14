import { Routes } from '@angular/router'

export const chatRoutes: Routes = [
  {
    path: 'chats',
    loadComponent: () => import('./pages').then(c => c.ConversationList),
  },
  {
    path: 'chats/:id',
    loadComponent: () => import('./pages').then(c => c.ConversationPage),
  },
]
