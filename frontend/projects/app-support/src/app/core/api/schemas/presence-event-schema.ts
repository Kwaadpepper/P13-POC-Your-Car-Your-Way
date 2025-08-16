import { z } from 'zod'

import { uuidSchema } from '@ycyw/shared'
import { PresenceStatus, Role } from '@ycyw/support-shared/enums'

const presenceEventSchema = z.object({
  user: uuidSchema,
  role: z.nativeEnum(Role),
  status: z.nativeEnum(PresenceStatus),
  conversation: uuidSchema,
})

export type PresenceEventZod = z.infer<typeof presenceEventSchema>

export default presenceEventSchema
