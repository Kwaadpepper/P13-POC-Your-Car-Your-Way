import { Injectable, inject, resource } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Faq, FaqId } from '~support-domains/faq/models'

import { FaqRepositoryInjector } from '../repositories'

@Injectable({ providedIn: 'root' })
export class FaqStore {
  private readonly repository = inject(FaqRepositoryInjector)
  private readonly _faqs = resource({
    defaultValue: [],
    loader: this.reloadAll.bind(this),
  })

  readonly faqs = this._faqs.value.asReadonly()

  async getFaq(id: FaqId): Promise<Faq | null> {
    const faq = this._faqs.value().find(f => f.id === id)
      ?? (await this.reloadAll()).find(f => f.id === id) ?? null
    return faq
  }

  async reloadAll() {
    try {
      const faqs = await firstValueFrom(this.repository.getFaqs())
      return faqs
    }
    catch (error) {
      console.error('Failed to reload FAQs:', error)
      return []
    }
  }
}
