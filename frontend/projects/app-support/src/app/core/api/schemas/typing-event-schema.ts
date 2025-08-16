import { z } from 'zod'

import { uuidSchema } from '@ycyw/shared'
import { Role } from '@ycyw/support-shared/enums'

const typingEventSchema = z.object({
  user: uuidSchema,
  role: z.nativeEnum(Role),
  conversation: uuidSchema,
  typing: z.coerce.boolean(),
})

export type TypingEventZod = z.infer<typeof typingEventSchema>

export default typingEventSchema
