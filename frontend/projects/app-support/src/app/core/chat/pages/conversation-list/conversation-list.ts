import { DatePipe } from '@angular/common'
import { Component, computed, inject } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { AvatarModule } from 'primeng/avatar'
import { BadgeModule } from 'primeng/badge'
import { ButtonModule } from 'primeng/button'
import { ListboxModule } from 'primeng/listbox'
import { MessageModule } from 'primeng/message'

import { Conversation } from '~support-domains/chat/models'
import { BackButton } from '~support-shared/components'

import { ConversationListViewModel } from './conversation-list-viewmodel'

@Component({
  selector: 'support-conversation-list',
  imports: [
    AvatarModule,
    BadgeModule,
    ButtonModule,
    ListboxModule,
    MessageModule,
    DatePipe,
    BackButton,
  ],
  providers: [ConversationListViewModel],
  templateUrl: './conversation-list.html',
  styleUrl: './conversation-list.css',
})
export class ConversationList {
  private readonly router = inject(Router)
  private readonly route = inject(ActivatedRoute)

  private readonly viewModel = inject(ConversationListViewModel)
  readonly conversations = computed(() => this.viewModel.conversations())

  open(item: Conversation) {
    this.router.navigate(['.', item.id], { relativeTo: this.route })
  }

  avatarLabel(item: Conversation): string {
    const t = item.subject || ''
    return t.slice(0, 2).toUpperCase()
  }
}
