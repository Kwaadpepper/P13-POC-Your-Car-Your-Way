import { Entity } from './entity.type'

export default interface PageOf<T extends Entity> {
  list: T[]
  page: number
  totalPages: number
  totalItems: number
}
