import { z } from 'zod'

import { Role } from '~support-shared/enums'

const messageSchema = z.object({
  id: z.string().uuid(),
  conversation: z.string().uuid(),
  from: z.object({
    id: z.string().uuid(),
    name: z.string().nonempty(),
    role: z.nativeEnum(Role),
  }),
  text: z.string(),
  sentAt: z.coerce.date(),
})

export type MessageZod = z.infer<typeof messageSchema>

export default messageSchema
