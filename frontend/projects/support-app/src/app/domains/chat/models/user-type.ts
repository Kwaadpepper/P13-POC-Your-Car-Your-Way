import { Entity } from '~ycyw/shared'

import { Role } from '../enums'

export interface User extends Entity {
  name: string
  role: Role
}

export type UserId = User['id']
