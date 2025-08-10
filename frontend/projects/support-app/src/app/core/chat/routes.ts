import { Conversation, ConversationList } from './pages'

export const chatRoutes = [
  {
    path: 'chats',
    component: ConversationList,
  },
  {
    path: 'chats/:id',
    component: Conversation,
  },
]
