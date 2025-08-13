import { z } from 'zod'

export const validationErrorSchema = z.object({
  timestamp: z.date({ coerce: true }),
  message: z.string().min(1),
  errors: z.record(z.string().min(1), z.string().min(1)),
  uri: z.string().min(1),
})

export type ValidationErrorZod = z.infer<typeof validationErrorSchema>
