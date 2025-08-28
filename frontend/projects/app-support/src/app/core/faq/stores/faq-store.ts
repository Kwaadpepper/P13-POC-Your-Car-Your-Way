import { Injectable, inject, signal } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { Faq, FaqId } from '@ycyw/support-domains/faq/models'
import { FAQ_REPOSITORY } from '@ycyw/support-tokens/faq-repository-token'

@Injectable({
  providedIn: 'root',
  deps: [FAQ_REPOSITORY],
})
export class FaqStore {
  private readonly repository = inject(FAQ_REPOSITORY)
  private readonly _faqs = signal<Faq[]>([])

  readonly faqs = this._faqs.asReadonly()

  async getFaq(id: FaqId): Promise<Faq | null> {
    const faq = this.faqs().find(f => f.id === id)
      ?? (await this.loadFaqs()).find(f => f.id === id) ?? null
    return faq
  }

  reloadAll() {
    this.loadFaqs()
  }

  private async loadFaqs() {
    return await firstValueFrom(this.repository.getAll())
  }
}
