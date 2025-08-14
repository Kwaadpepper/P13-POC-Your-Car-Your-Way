import { DatePipe } from '@angular/common'
import { Component, computed, inject, input } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { ProgressSpinnerModule } from 'primeng/progressspinner'
import { TagModule } from 'primeng/tag'

import { Issue } from '~support-domains/issue/models'
import { BackButton, ChatBox } from '~support-shared/components'

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
    BackButton,
    ChatBox,
  ],
  providers: [IssuePageViewModel],
  templateUrl: './issue-page.html',
  styleUrl: './issue-page.css',
})
export class IssuePage {
  private readonly route = inject(ActivatedRoute)
  readonly router = inject(Router)
  readonly viewModel = inject(IssuePageViewModel)

  readonly issue = input.required<Issue>()
  readonly conversation = computed(() => this.issue().conversation)

  openChat() {
    this.router.navigate(
      ['.', this.issue().id],
      { relativeTo: this.route },
    )
  }
}
