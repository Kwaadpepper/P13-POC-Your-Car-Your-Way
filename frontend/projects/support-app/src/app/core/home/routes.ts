import { ChatDetail, ChatWindow } from '~support-domains/chat/pages'

// NOTE: Cannot import using TS aliases here.
// Always use relative imports in files exposed via Module Federation!

export const homeRoutes = [
  {
    path: 'chat',
    component: ChatWindow,
  },
  {
    path: 'chat/:id',
    component: ChatDetail,
  },
]
