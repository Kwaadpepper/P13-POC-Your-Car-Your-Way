import { z } from 'zod'

import faqSchema from './faq-schema'

const faqListSchema = z.array(faqSchema)

export type FaqListZod = z.infer<typeof faqListSchema>

export default faqListSchema
