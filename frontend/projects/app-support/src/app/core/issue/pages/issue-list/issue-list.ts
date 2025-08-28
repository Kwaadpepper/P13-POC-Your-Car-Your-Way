import { DatePipe } from '@angular/common'
import { Component, computed, inject } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { AvatarModule } from 'primeng/avatar'
import { BadgeModule } from 'primeng/badge'
import { ButtonModule } from 'primeng/button'
import { ListboxModule } from 'primeng/listbox'
import { MessageModule } from 'primeng/message'
import { ProgressSpinnerModule } from 'primeng/progressspinner'

import { Conversation } from '@ycyw/support-domains/chat/models'
import { BackButton } from '@ycyw/support-shared/components'

import { IssueListViewModel } from './issue-list-viewmodel'

@Component({
  selector: 'support-issue-list',
  imports: [
    AvatarModule,
    BadgeModule,
    ButtonModule,
    ListboxModule,
    MessageModule,
    DatePipe,
    BackButton,
    ProgressSpinnerModule,
  ],
  providers: [
    IssueListViewModel,
  ],
  templateUrl: './issue-list.html',
  styleUrl: './issue-list.css',
})
export class IssueList {
  private readonly router = inject(Router)
  private readonly route = inject(ActivatedRoute)

  private readonly viewModel = inject(IssueListViewModel)

  readonly issues = computed(() => this.viewModel.issues())
  readonly loading = this.viewModel.loading
  readonly loadingError = this.viewModel.loadingError

  open(item: Conversation) {
    this.router.navigate(['.', item.id], { relativeTo: this.route })
  }

  avatarLabel(item: Conversation): string {
    const t = item.subject || ''
    return t.slice(0, 2).toUpperCase()
  }
}
