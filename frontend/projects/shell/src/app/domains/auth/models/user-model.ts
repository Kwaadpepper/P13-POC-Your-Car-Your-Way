import { Entity } from '@ycyw/shared'
import { Role } from '@ycyw/shell-shared/enums'

export interface User extends Entity {
  name: string
  role: Role
  email: string
  createdAt: Date
  updatedAt: Date
}
