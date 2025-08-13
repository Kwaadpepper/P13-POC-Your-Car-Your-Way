import { z } from 'zod'

import { Role } from '~support-shared/enums'
import { uuidSchema } from '~ycyw/shared'

const typingEventSchema = z.object({
  user: uuidSchema,
  role: z.nativeEnum(Role),
  conversation: uuidSchema,
  typing: z.coerce.boolean(),
})

export type TypingEventZod = z.infer<typeof typingEventSchema>

export default typingEventSchema
