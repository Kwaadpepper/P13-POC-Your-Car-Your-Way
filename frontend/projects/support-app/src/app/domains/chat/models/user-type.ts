import { Entity } from '~ycyw/shared'

export interface User extends Entity {
  name: string
}

export type UserId = User['id']
