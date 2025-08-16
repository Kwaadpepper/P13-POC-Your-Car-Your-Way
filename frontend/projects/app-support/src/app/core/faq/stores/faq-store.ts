import { Injectable, computed, inject, resource } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Faq, FaqId } from '@ycyw/support-domains/faq/models'
import { FAQ_REPOSITORY } from '@ycyw/support-tokens/faq-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [FAQ_REPOSITORY],
})
export class FaqStore {
  private readonly repository = inject(FAQ_REPOSITORY)
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
    return await firstValueFrom(this.repository.getAll())
  }
}
