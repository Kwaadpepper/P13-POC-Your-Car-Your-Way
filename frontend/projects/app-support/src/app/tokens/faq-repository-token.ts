import { InjectionToken } from '@angular/core'

import { FaqRepositoryImpl } from '@ycyw/support-core/faq/repositories'
import { FaqRepository } from '@ycyw/support-domains/faq/repositories'

export const FAQ_REPOSITORY = new InjectionToken<FaqRepository>('FaqRepositoryInjector', {
  providedIn: 'root',
  factory: () => new FaqRepositoryImpl(),
})
