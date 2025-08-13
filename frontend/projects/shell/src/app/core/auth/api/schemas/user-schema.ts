import { z } from 'zod'

import { Role } from '~shell-shared/enums'

export const userSchema = z.object({
  id: z.string().uuid(),
  name: z.string().min(1),
  role: z.nativeEnum(Role),
  email: z.string().email(),
  createdAt: z.date({ coerce: true }),
  updatedAt: z.date({ coerce: true }),
})

export type UserZod = z.infer<typeof userSchema>
