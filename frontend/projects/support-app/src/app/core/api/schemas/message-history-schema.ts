import { z } from 'zod'

import messageSchema from './message-schema'

const messageHistorySchema = z.array(messageSchema)

export type MessageHistoryZod = z.infer<typeof messageHistorySchema>

export default messageHistorySchema
