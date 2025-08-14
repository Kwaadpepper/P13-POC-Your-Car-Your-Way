import { Observable } from 'rxjs'

import { Entity } from '../types'

export interface Repository<E extends Entity, I = Entity['id']> {
  readonly resourceUrl: string

  getAll?(): Observable<E[]>

  get?(id: I): Observable<E | null>

  create?(entity: E): Observable<I>

  update?(entity: E): Observable<void>

  delete?(id: I): Observable<void>
}
