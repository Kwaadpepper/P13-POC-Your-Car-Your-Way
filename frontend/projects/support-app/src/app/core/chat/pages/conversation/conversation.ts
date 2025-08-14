import { Component, OnInit, inject, signal } from '@angular/core'
import { NotFoundError } from '@angular/core/primitives/di'
import { ActivatedRoute } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { TagModule } from 'primeng/tag'
import { ToolbarModule } from 'primeng/toolbar'

import { BackButton, ChatBox } from '~support-shared/components'
import { UUID, uuidSchema } from '~ycyw/shared'

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
export class ConversationPage implements OnInit {
  private readonly route = inject(ActivatedRoute)

  readonly viewmodel = inject(ConversationViewModel)

  readonly _conversationId = signal<UUID | null>(null)
  readonly conversationId = this._conversationId.asReadonly()

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      try {
        const id: UUID = uuidSchema.parse(params.get('id'))
        this._conversationId.set(id)
      }
      catch {
        throw new NotFoundError('No conversation found')
      }
    })
  }
}
