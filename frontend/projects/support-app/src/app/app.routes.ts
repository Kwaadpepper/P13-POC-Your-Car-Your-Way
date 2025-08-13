import { Routes } from '@angular/router'

import { chatRoutes } from '~support-core/chat/routes'
import { faqsRoutes } from '~support-core/faq/routes'
import { homeRoutes } from '~support-core/home/routes'
import { issueRoutes } from '~support-core/issue/routes'

export const routes: Routes = [
  ...homeRoutes,
  ...faqsRoutes,
  ...issueRoutes,
  ...chatRoutes,
]
