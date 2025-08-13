import { z } from 'zod'

import { uuidSchema } from '~ycyw/shared'

const faqSchema = z.object({
  id: uuidSchema,
  question: z.string().nonempty(),
  answer: z.string().nonempty(),
  type: z.string().nonempty(),
})

export type FaqZod = z.infer<typeof faqSchema>

export default faqSchema
