import { Routes } from '@angular/router'

import { conversationResolver } from './resolvers'

export const chatRoutes: Routes = [
  {
    path: 'chats',
    title: 'Conversations',
    loadComponent: () => import('./pages').then(c => c.ConversationList),
  },
  {
    path: 'chats/:id',
    title: () => 'Conversation ' + window.location.pathname.split('/').pop(),
    loadComponent: () => import('./pages').then(c => c.ConversationPage),
    resolve: {
      conversation: conversationResolver,
    },
  },
]
