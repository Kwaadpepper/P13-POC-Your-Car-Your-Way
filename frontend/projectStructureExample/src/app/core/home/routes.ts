import tchat from '../../domains/tchat'

// NOTE: Cannot import using TS aliases here.
// Always use relative imports in files exposed via Module Federation!

export = [
  {
    path: 'chat',
    component: tchat.pages.TchatWindow,
  },
  {
    path: 'chat/:id',
    component: tchat.pages.TchatDetail,
  },
]
