import { CommonModule } from '@angular/common'
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core'
import { FormsModule } from '@angular/forms'

import { AccordionModule } from 'primeng/accordion'
import { ButtonModule } from 'primeng/button'
import { InputTextModule } from 'primeng/inputtext'
import { SelectModule } from 'primeng/select'
import { SkeletonModule } from 'primeng/skeleton'
import { TagModule } from 'primeng/tag'
import { ToggleButtonModule } from 'primeng/togglebutton'
import { TooltipModule } from 'primeng/tooltip'

import { Faq } from '~support-domains/faq/models'
import { BackButton } from '~support-shared/components'

import { FaqListViewModel } from './faq-list-viewmodel'

@Component({
  selector: 'support-faq-page',
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    SelectModule,
    ToggleButtonModule,
    ButtonModule,
    AccordionModule,
    TagModule,
    SkeletonModule,
    TooltipModule,
    BackButton,
  ],
  providers: [
    FaqListViewModel,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './faq-list.html',
  styleUrl: './faq-list.css',
})
export class FaqList {
  readonly viewModel = inject(FaqListViewModel)

  readonly searchValue = signal('')
  readonly selectedType = signal<Faq['type'] | null>(null)
  readonly groupByType = signal(false)

  readonly availableTypes = computed(() => {
    const set = new Set(this.viewModel.faqs().map(f => f.type).filter(Boolean))
    return Array.from(set).sort((a, b) => a.localeCompare(b))
  })

  readonly filteredFaqs = computed(() => {
    const term = this.searchValue().trim().toLowerCase()

    return this.viewModel.faqs().filter((f) => {
      const okType = this.selectedType() === null
        || f.type === this.selectedType()
      if (!okType) return false
      if (!term) return true
      return f.question.toLowerCase().includes(term)
        || f.answer.toLowerCase().includes(term)
    })
  })

  readonly groupedFaqs = computed(() => {
    if (!this.groupByType()) return []
    const map = new Map<string, Faq[]>()
    for (const f of this.filteredFaqs()) {
      if (!map.has(f.type)) {
        map.set(f.type, [])
      }
      map.get(f.type)!.push(f)
    }
    return Array.from(map.entries())
      .map(([type, faqs]) => ({ type, faqs }))
      .sort((a, b) => a.type.localeCompare(b.type))
  })

  readonly faqTypeOptions = computed(() => this.availableTypes().map(t => ({ label: t, value: t })))

  readonly skeletonItems = [1, 2, 3, 4, 5]

  toggleGroupByType() {
    this.groupByType.update(x => !x)
  }
}
