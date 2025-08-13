import { z } from 'zod'

export const simpleMessageSchema = z.object({
  message: z.string(),
})

export type SimpleMessageZod = z.infer<typeof simpleMessageSchema>
