import { CommonModule } from '@angular/common'
import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core'
import { FormsModule } from '@angular/forms'

import { AccordionModule } from 'primeng/accordion'
import { ButtonModule } from 'primeng/button'
import { InputTextModule } from 'primeng/inputtext'
import { MessageModule } from 'primeng/message'
import { SelectModule } from 'primeng/select'
import { SkeletonModule } from 'primeng/skeleton'
import { TagModule } from 'primeng/tag'
import { ToggleButtonModule } from 'primeng/togglebutton'
import { TooltipModule } from 'primeng/tooltip'

import { Faq } from '@ycyw/support-domains/faq/models'
import { BackButton } from '@ycyw/support-shared/components'

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
    MessageModule,
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

  readonly loading = this.viewModel.loading
  readonly loadingError = this.viewModel.loadingError

  readonly searchValue = signal('')
  readonly selectedType = signal<Faq['category'] | null>(null)
  readonly groupByType = signal(false)

  readonly filteredFaqs = computed(() => {
    const term = this.searchValue().trim().toLowerCase()

    return this.viewModel.getFaqsFrom(term, this.selectedType())
  })

  readonly groupedFaqs = computed(() => {
    if (!this.groupByType()) return []
    const map = new Map<string, Faq[]>()
    for (const f of this.filteredFaqs()) {
      if (!map.has(f.category)) {
        map.set(f.category, [])
      }
      map.get(f.category)!.push(f)
    }
    return Array.from(map.entries())
      .map(([type, faqs]) => ({ type, faqs }))
      .sort((a, b) => a.type.localeCompare(b.type))
  })

  readonly faqTypeOptions = computed(() =>
    this.viewModel.availableTypes()
      .map(t => ({ label: t, value: t })),
  )

  readonly skeletonItems = [1, 2, 3, 4, 5]

  toggleGroupByType() {
    this.groupByType.update(x => !x)
  }
}
