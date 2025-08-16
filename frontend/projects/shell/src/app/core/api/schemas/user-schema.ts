import { z } from 'zod'

import { uuidSchema } from '@ycyw/shared'
import { Role } from '@ycyw/shell-shared/enums'

export const userSchema = z.object({
  id: uuidSchema,
  name: z.string().min(1),
  role: z.nativeEnum(Role),
  email: z.string().email(),
  createdAt: z.date({ coerce: true }),
  updatedAt: z.date({ coerce: true }),
})

export type UserZod = z.infer<typeof userSchema>
