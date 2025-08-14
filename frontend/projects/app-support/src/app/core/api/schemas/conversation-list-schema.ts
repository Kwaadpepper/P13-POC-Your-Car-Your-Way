import { z } from 'zod'

import conversationSchema from './conversation-schema'

const conversationListSchema = z.array(conversationSchema)

export type ConversationListZod = z.infer<typeof conversationListSchema>

export default conversationListSchema
