import { InjectionToken } from '@angular/core'

import { SupportConfigServiceImpl } from '~support-core/home/services'
import { SupportConfigService } from '~support-domains/support/services'

export const SUPPORT_CONFIG_SERVICE = new InjectionToken<SupportConfigService>('SupportConfigServiceInjector', {
  providedIn: 'root',
  factory: () => new SupportConfigServiceImpl(),
})
