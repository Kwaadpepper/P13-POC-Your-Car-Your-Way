import { Routes } from '@angular/router'

import { Conversation, ConversationList } from './pages'

export const chatRoutes: Routes = [
  {
    path: 'chats',
    component: ConversationList,
  },
  {
    path: 'chats/:id',
    component: Conversation,
  },
]
