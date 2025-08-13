import { z } from 'zod'

import { UUID } from '../types'

export const uuidSchema = z.string().uuid().transform(v => v as UUID)

export type UUIDZod = z.infer<typeof uuidSchema>
