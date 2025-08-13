import { Role } from '~shell-shared/enums'
import { Entity } from '~ycyw/shared'

export interface User extends Entity {
  name: string
  role: Role
  email: string
  createdAt: Date
  updatedAt: Date
}
