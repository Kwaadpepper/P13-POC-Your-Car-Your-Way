import { Component, inject, input } from '@angular/core'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { TagModule } from 'primeng/tag'
import { ToolbarModule } from 'primeng/toolbar'

import { Conversation } from '~support-domains/chat/models'
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
export class ConversationPage {
  readonly viewmodel = inject(ConversationViewModel)

  readonly conversation = input.required<Conversation>()
}
