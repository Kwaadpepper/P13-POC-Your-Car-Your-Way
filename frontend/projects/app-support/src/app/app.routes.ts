import { Routes } from '@angular/router'

import { chatRoutes } from '@ycyw/support-core/chat/routes'
import { faqsRoutes } from '@ycyw/support-core/faq/routes'
import { homeRoutes } from '@ycyw/support-core/home/routes'
import { issueRoutes } from '@ycyw/support-core/issue/routes'

export const routes: Routes = [
  ...homeRoutes,
  ...faqsRoutes,
  ...issueRoutes,
  ...chatRoutes,
]
