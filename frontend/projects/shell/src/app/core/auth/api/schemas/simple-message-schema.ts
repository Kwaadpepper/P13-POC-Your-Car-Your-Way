import { z } from 'zod'

const simpleMessageSchema = z.object({
  message: z.string(),
})

export type SimpleMessageZod = z.infer<typeof simpleMessageSchema>

export default simpleMessageSchema
