import { z } from 'zod'

import { uuidSchema } from '@ycyw/shared'
import { Role } from '@ycyw/support-shared/enums'

const messageSchema = z.object({
  id: uuidSchema,
  conversation: uuidSchema,
  from: z.object({
    id: uuidSchema,
    role: z.nativeEnum(Role),
  }),
  text: z.string(),
  sentAt: z.coerce.date(),
})

export type MessageZod = z.infer<typeof messageSchema>

export default messageSchema
