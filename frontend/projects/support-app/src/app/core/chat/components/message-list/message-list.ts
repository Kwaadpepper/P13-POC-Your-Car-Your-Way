import { AfterViewInit, Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild } from '@angular/core'

import { ScrollPanelModule } from 'primeng/scrollpanel'

import { ChatMessage, UserId } from '~support-domains/chat/models'

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
  @Input() messages: ChatMessage[] = []
  @Input() currentUser!: UserId
  @ViewChild('panel') panel?: ElementRef

  ngAfterViewInit(): void {
    this.scrollToBottom()
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['messages']) this.scrollToBottom()
  }

  private scrollToBottom() {
    setTimeout(() => {
      const el = document.querySelector('.sp .p-scrollpanel-content')
      if (el) el.scrollTop = el.scrollHeight
    })
  }
}
