import { loadRemoteModule } from '@angular-architects/native-federation'

// NOTE: Cannot import using TS aliases here.
// Always use relative imports in files exposed via Module Federation!

export const homeRoutes = [
  {
    path: 'support',
    loadChildren: () =>
      loadRemoteModule({
        remoteName: 'support-app',
        exposedModule: './routes',
      })
        .then(m => m.routes),
  },
]
