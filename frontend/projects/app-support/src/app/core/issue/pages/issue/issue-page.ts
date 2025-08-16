import { DatePipe } from '@angular/common'
import { ChangeDetectionStrategy, Component, Injector, computed, effect, inject, input, signal } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { ActivatedRoute, Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { ProgressSpinnerModule } from 'primeng/progressspinner'
import { TabsModule } from 'primeng/tabs'
import { TagModule } from 'primeng/tag'
import { map } from 'rxjs/operators'

import { Issue } from '@ycyw/support-domains/issue/models'
import { BackButton, ChatBox } from '@ycyw/support-shared/components'

import { IssuePageViewModel } from './issue-page-viewmodel'

@Component({
  selector: 'support-issue-page',
  imports: [
    DatePipe,
    ButtonModule,
    TagModule,
    MessageModule,
    ProgressSpinnerModule,
    DividerModule,
    TabsModule,
    BackButton,
    ChatBox,
  ],
  providers: [IssuePageViewModel],
  templateUrl: './issue-page.html',
  styleUrl: './issue-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IssuePage {
  private readonly route = inject(ActivatedRoute)
  private readonly router = inject(Router)
  private readonly injector = inject(Injector)
  readonly viewModel = inject(IssuePageViewModel)

  readonly issue = input.required<Issue>()
  readonly conversation = computed(() => this.issue().conversation)

  // Onglets
  readonly detailsTabIndex = 'details'
  readonly chatTabIndex = 'chat'
  readonly activeTab = signal<string>(this.detailsTabIndex)

  private readonly tabParam = toSignal(
    this.route.queryParamMap.pipe(map(qp => qp.get('tab'))),
    { initialValue: this.route.snapshot.queryParamMap.get('tab') },
  )

  constructor() {
    // URL -> UI
    effect(
      () => {
        const tab = this.tabParam()
        const desiredIndex = tab === 'chat' ? this.chatTabIndex : this.detailsTabIndex
        if (this.activeTab() !== desiredIndex) {
          this.activeTab.set(desiredIndex)
        }
      },
      { injector: this.injector },
    )
  }

  // UI -> URL
  onTabChange(index: string) {
    if (this.activeTab() !== index) {
      this.activeTab.set(index)
    }
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tab: index === this.chatTabIndex ? 'chat' : null },
      queryParamsHandling: 'merge',
      replaceUrl: true,
    })
  }

  openChat() {
    this.onTabChange(this.chatTabIndex)
  }
}
