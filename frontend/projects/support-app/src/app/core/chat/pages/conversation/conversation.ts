import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { TagModule } from 'primeng/tag'
import { ToolbarModule } from 'primeng/toolbar'

import { MessageInput, MessageList, TypingIndicator } from '~support-core/chat/components'
import { UserId } from '~support-domains/chat/models'

import { ConversationViewModel } from './conversation-viewmodel'

@Component({
  selector: 'support-conversation',
  standalone: true,
  providers: [ConversationViewModel],
  imports: [
    ToolbarModule,
    ButtonModule,
    TagModule,
    DividerModule,
    MessageList,
    MessageInput,
    TypingIndicator,
  ],
  templateUrl: './conversation.html',
  styleUrl: './conversation.css',
})
export class Conversation implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute)
  private readonly router = inject(Router)

  readonly viewModel = inject(ConversationViewModel)

  // TODO: récupère depuis l'auth
  readonly currentUserId = signal<UserId>('client_7b8e42ff-4471-429d-9f1a-cb3b220cdb16').asReadonly()

  get onlineCount(): number {
    return this.viewModel.participants().filter(p => p.status === 'online').length
  }

  ngOnInit() {
    this.viewModel.init(this.currentUserId())

    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')
      if (id) this.viewModel.join(id)
    })
  }

  ngOnDestroy(): void {
    this.viewModel.leave()
    this.viewModel.destroy()
  }

  onSend(text: string) {
    this.viewModel.sendMessage(text)
  }

  onTyping(isTyping: boolean) {
    this.viewModel.setTyping(isTyping)
  }

  backToList() {
    this.router.navigate(['/chats'])
  }
}
