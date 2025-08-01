import Entity from '../interfaces/Entity.interface'

export default interface PageOf<T extends Entity> {
  list: T[]
  page: number
  totalPages: number
  totalItems: number
}
