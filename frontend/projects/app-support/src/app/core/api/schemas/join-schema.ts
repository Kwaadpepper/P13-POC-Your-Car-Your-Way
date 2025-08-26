import { z } from 'zod'

import { uuidSchema } from '@ycyw/shared'
import { PresenceStatus, Role } from '@ycyw/support-shared/enums'

const joinSchema = z.object({
  conversation: uuidSchema,
  participants: z.array(z.object({
    user: uuidSchema,
    role: z.nativeEnum(Role),
    status: z.nativeEnum(PresenceStatus),
  })),
})

export type JoinZod = z.infer<typeof joinSchema>

export default joinSchema
