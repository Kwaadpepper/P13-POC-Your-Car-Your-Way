import { Signal } from '@angular/core'

import { Observable } from 'rxjs'

import { SessionSnapshot } from './types'

export interface SessionStoreReadonly {
  readonly session: Signal<SessionSnapshot>
  readonly session$: Observable<SessionSnapshot>
  snapshot(): SessionSnapshot
}
