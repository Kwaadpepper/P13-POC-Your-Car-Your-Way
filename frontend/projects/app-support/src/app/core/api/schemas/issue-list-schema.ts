import { z } from 'zod'

import { issueSchema } from './issue-schema'

const issueListSchema = z.array(issueSchema)

export type IssueListZod = z.infer<typeof issueListSchema>

export default issueListSchema
