import { inject } from '@angular/core'
import { NotFoundError } from '@angular/core/primitives/di'
import type { ActivatedRouteSnapshot, ResolveFn } from '@angular/router'

import { Issue } from '~support-domains/issue/models'
import { UUID, uuidSchema } from '~ycyw/shared'

import { IssueStore } from '../stores/issue-store'

export const issueResolver: ResolveFn<Issue>
  = async (route: ActivatedRouteSnapshot) => {
    try {
      const store = inject(IssueStore)
      const routeId = route.paramMap.get('id')

      if (!routeId) {
        throw new NotFoundError('No issue ID provided')
      }

      const id: UUID = uuidSchema.parse(routeId)

      const issue = await store.getIssue(id)

      if (!issue) {
        throw new NotFoundError('No issue found')
      }

      return issue
    }
    catch {
      throw new NotFoundError('No issue found')
    }
  }
