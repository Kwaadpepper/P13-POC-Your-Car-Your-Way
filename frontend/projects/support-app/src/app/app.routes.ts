import { Routes } from '@angular/router'

import { chatRoutes } from '~support-core/chat/routes'
import { homeRoutes } from '~support-core/home/routes'
import { issueRoutes } from '~support-core/issue/routes'

export const routes: Routes = [
  ...homeRoutes,
  ...issueRoutes,
  ...chatRoutes,
]
