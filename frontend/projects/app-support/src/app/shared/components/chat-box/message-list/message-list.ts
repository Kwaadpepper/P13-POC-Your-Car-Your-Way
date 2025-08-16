import { AfterViewInit, Component, ElementRef, input, OnChanges, SimpleChanges, ViewChild } from '@angular/core'

import { ScrollPanel, ScrollPanelModule } from 'primeng/scrollpanel'

import { UUID } from '@ycyw/shared'
import { ChatMessage } from '@ycyw/support-domains/chat/models'

import { Message } from '../message/message'

@Component({
  selector: 'support-message-list',
  imports: [
    ScrollPanelModule,
    Message,
  ],
  templateUrl: './message-list.html',
  styleUrl: './message-list.css',
})
export class MessageList implements AfterViewInit, OnChanges {
  readonly messages = input.required<ChatMessage[]>()
  readonly currentUser = input.required<UUID>()
  @ViewChild('panel') panel?: ScrollPanel

  ngAfterViewInit(): void {
    this.scrollToBottom()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['messages']) this.scrollToBottom()
  }

  private scrollToBottom() {
    setTimeout(() => {
      const el = this.panel?.contentViewChild as ElementRef<HTMLElement> | undefined
      if (!el) {
        console.error('No panel element found to scroll')
        return
      }
      el.nativeElement.scrollIntoView({
        block: 'end',
        behavior: 'smooth',
      })
    })
  }
}
