import { z } from 'zod'

import { uuidSchema } from '~ycyw/shared'

const conversationSchema = z.object({
  id: uuidSchema,
  subject: z.string().nonempty(),
  lastMessage: z.object({
    content: z.string().nonempty(),
    sentAt: z.coerce.date(),
  }).optional(),
})

export type ConversationZod = z.infer<typeof conversationSchema>

export default conversationSchema
