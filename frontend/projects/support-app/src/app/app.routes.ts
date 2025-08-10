import { Routes } from '@angular/router'

import { chatRoutes } from '~support-core/chat/routes'
import { homeRoutes } from '~support-core/home/routes'

export const routes: Routes = [
  ...homeRoutes,
  ...chatRoutes,
]
