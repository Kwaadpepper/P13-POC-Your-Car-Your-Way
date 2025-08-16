import { inject } from '@angular/core'
import { NotFoundError } from '@angular/core/primitives/di'
import type { ActivatedRouteSnapshot, ResolveFn } from '@angular/router'

import { UUID, uuidSchema } from '@ycyw/shared'
import { Conversation } from '@ycyw/support-domains/chat/models'

import { ConversationStore } from '../stores'

export const conversationResolver: ResolveFn<Conversation>
  = async (route: ActivatedRouteSnapshot) => {
    try {
      const store = inject(ConversationStore)
      const routeId = route.paramMap.get('id')

      if (!routeId) {
        throw new NotFoundError('No conversation ID provided')
      }

      const id: UUID = uuidSchema.parse(routeId)

      const conversation = await store.getConversation(id)

      if (!conversation) {
        throw new NotFoundError('No conversation found')
      }

      return conversation
    }
    catch {
      throw new NotFoundError('No conversation found')
    }
  }
