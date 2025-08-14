import { z } from 'zod'

import { Role } from '~support-shared/enums'
import { uuidSchema } from '~ycyw/shared'

const messageSchema = z.object({
  id: uuidSchema,
  conversation: uuidSchema,
  from: z.object({
    id: uuidSchema,
    name: z.string().nonempty(),
    role: z.nativeEnum(Role),
  }),
  text: z.string(),
  sentAt: z.coerce.date(),
})

export type MessageZod = z.infer<typeof messageSchema>

export default messageSchema
