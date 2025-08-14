import { z } from 'zod'

import { PresenceStatus, Role } from '~support-shared/enums'
import { uuidSchema } from '~ycyw/shared'

const presenceEventSchema = z.object({
  user: uuidSchema,
  role: z.nativeEnum(Role),
  status: z.nativeEnum(PresenceStatus),
  conversation: uuidSchema,
})

export type PresenceEventZod = z.infer<typeof presenceEventSchema>

export default presenceEventSchema
