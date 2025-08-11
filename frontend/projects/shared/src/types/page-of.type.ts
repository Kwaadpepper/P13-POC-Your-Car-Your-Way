import { Entity } from './entity.type'

export interface PageOf<T extends Entity> {
  list: T[]
  page: number
  totalPages: number
  totalItems: number
}
