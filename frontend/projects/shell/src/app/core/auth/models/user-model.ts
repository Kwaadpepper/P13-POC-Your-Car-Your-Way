import { Entity } from '@ycyw/shared'

export interface User extends Entity {
  name: string
  email: string
  createdAt: Date
  updatedAt: Date
}
