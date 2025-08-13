import { z } from 'zod'

import { PresenceStatus, Role } from '~support-domains/chat/enums'

const presenceEventSchema = z.object({
  user: z.string().uuid(),
  role: z.nativeEnum(Role),
  status: z.nativeEnum(PresenceStatus),
  conversation: z.string().uuid(),
})

export type PresenceEventZod = z.infer<typeof presenceEventSchema>

export default presenceEventSchema
