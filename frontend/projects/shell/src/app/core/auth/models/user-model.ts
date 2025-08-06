import { Entity } from '@ycyw/shared'

export interface User extends Entity {
  name: string
  email: string
  created_at: Date
  updated_at: Date
}
