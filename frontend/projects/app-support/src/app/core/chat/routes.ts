import { Routes } from '@angular/router'

import { conversationResolver } from './resolvers'

export const chatRoutes: Routes = [
  {
    path: 'chats',
    loadComponent: () => import('./pages').then(c => c.ConversationList),
  },
  {
    path: 'chats/:id',
    loadComponent: () => import('./pages').then(c => c.ConversationPage),
    resolve: {
      conversation: conversationResolver,
    },
  },
]
