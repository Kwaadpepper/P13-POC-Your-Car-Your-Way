import { Injectable, computed, inject, resource } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Faq, FaqId } from '~support-domains/faq/models'

import { FaqRepositoryInjector } from '../repositories'

@Injectable({ providedIn: 'root' })
export class FaqStore {
  private readonly repository = inject(FaqRepositoryInjector)
  private readonly _faqs = resource({
    defaultValue: [],
    loader: this.loadFaqs.bind(this),
  })

  readonly faqs = this._faqs.value.asReadonly()
  readonly loading = computed(() => this._faqs.isLoading())
  readonly error = computed(() => this._faqs.error())

  async getFaq(id: FaqId): Promise<Faq | null> {
    const faq = this._faqs.value().find(f => f.id === id)
      ?? (await this.loadFaqs()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this._faqs.reload()
  }

  private async loadFaqs() {
    return await firstValueFrom(this.repository.getFaqs())
  }
}
