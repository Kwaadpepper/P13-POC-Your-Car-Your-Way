import { Component, OnInit, inject, signal } from '@angular/core'
import { ActivatedRoute } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { TagModule } from 'primeng/tag'
import { ToolbarModule } from 'primeng/toolbar'

import { BackButton, ChatBox } from '~support-shared/components'

import { ConversationViewModel } from './conversation-viewmodel'

@Component({
  selector: 'support-conversation',
  imports: [
    ToolbarModule,
    ButtonModule,
    TagModule,
    DividerModule,
    MessageModule,
    ChatBox,
    BackButton,
  ],
  providers: [ConversationViewModel],
  templateUrl: './conversation.html',
  styleUrl: './conversation.css',
})
export class Conversation implements OnInit {
  private readonly route = inject(ActivatedRoute)

  readonly vm = inject(ConversationViewModel)

  readonly _conversationId = signal<string | null>(null)
  readonly conversationId = this._conversationId.asReadonly()

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')
      this._conversationId.set(id)
    })
  }
}
