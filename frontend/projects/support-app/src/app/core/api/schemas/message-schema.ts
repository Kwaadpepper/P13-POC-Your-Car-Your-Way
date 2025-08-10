import { z } from 'zod'

import { Role } from '~support-domains/chat/enums'

const messageSchema = z.object({
  id: z.string().uuid(),
  conversation: z.string().uuid(),
  from: z.object({
    id: z.string().uuid(),
    role: z.nativeEnum(Role),
  }),
  text: z.string(),
  sentAt: z.date(),
})

export type MessageZod = z.infer<typeof messageSchema>

export default messageSchema
