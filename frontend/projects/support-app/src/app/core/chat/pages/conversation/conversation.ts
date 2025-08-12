import { Component, OnDestroy, OnInit, inject } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { DividerModule } from 'primeng/divider'
import { MessageModule } from 'primeng/message'
import { TagModule } from 'primeng/tag'
import { ToolbarModule } from 'primeng/toolbar'

import { MessageInput, MessageList, TypingIndicator } from '~support-core/chat/components'

import { ConversationViewModel } from './conversation-viewmodel'

@Component({
  selector: 'support-conversation',
  imports: [
    ToolbarModule,
    ButtonModule,
    TagModule,
    DividerModule,
    MessageModule,
    MessageList,
    MessageInput,
    TypingIndicator,
  ],
  providers: [ConversationViewModel],
  templateUrl: './conversation.html',
  styleUrl: './conversation.css',
})
export class Conversation implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute)
  private readonly router = inject(Router)

  readonly viewModel = inject(ConversationViewModel)

  get onlineCount(): number {
    return this.viewModel.participants().filter(p => p.status === 'online').length
  }

  ngOnInit() {
    this.viewModel.init()

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
    this.router.navigate(['..'], { relativeTo: this.route })
  }
}
