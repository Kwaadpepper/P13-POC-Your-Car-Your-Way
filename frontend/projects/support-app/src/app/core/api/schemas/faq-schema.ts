import { z } from 'zod'

const faqSchema = z.object({
  id: z.string().uuid(),
  question: z.string().nonempty(),
  answer: z.string().nonempty(),
  type: z.string().nonempty(),
})

export type FaqZod = z.infer<typeof faqSchema>

export default faqSchema
