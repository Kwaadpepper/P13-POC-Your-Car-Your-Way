import Entity from './Entity.interface'

export default interface User extends Entity {
  name: string
  email: string
  created_at: Date
  updated_at: Date
}
