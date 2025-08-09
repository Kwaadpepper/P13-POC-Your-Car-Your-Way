import { TchatDetail, TchatWindow } from '~support-domains/tchat/pages'

// NOTE: Cannot import using TS aliases here.
// Always use relative imports in files exposed via Module Federation!

export const homeRoutes = [
  {
    path: 'chat',
    component: TchatWindow,
  },
  {
    path: 'chat/:id',
    component: TchatDetail,
  },
]
