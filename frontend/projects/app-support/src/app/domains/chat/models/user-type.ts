import { Role } from '~support-shared/enums'
import { Entity } from '~ycyw/shared'

export interface User extends Entity {
  name: string
  role: Role
}

export type UserId = User['id']
