import { z } from 'zod'

import { Role } from '~support-domains/chat/enums'

const typingEventSchema = z.object({
  user: z.string().uuid(),
  role: z.nativeEnum(Role),
  conversation: z.string().uuid(),
  typing: z.coerce.boolean(),
})

export type TypingEventZod = z.infer<typeof typingEventSchema>

export default typingEventSchema
