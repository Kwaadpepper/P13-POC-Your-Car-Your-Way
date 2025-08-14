import { Observable } from 'rxjs'

import { SupportConfig } from '../dtos'

export interface SupportConfigService {
  getConfig(): Observable<SupportConfig>
}
