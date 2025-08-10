import { DatePipe } from '@angular/common'
import { Component, inject, OnInit, signal } from '@angular/core'
import { Router } from '@angular/router'

import { AvatarModule } from 'primeng/avatar'
import { BadgeModule } from 'primeng/badge'
import { ListboxModule } from 'primeng/listbox'

import { Conversation } from '~support-domains/chat/models'

@Component({
  selector: 'support-conversation-list',
  imports: [
    AvatarModule,
    BadgeModule,
    ListboxModule,
    DatePipe,
  ],
  templateUrl: './conversation-list.html',
  styleUrl: './conversation-list.css',
})
export class ConversationList implements OnInit {
  private readonly router = inject(Router)

  readonly conversations = signal<Conversation[]>([])

  ngOnInit(): void {
    // TODO: remplacer par ton ConversationsRepository (Mockoon)
    this.conversations.set([
      {
        id: '7b8e42ff-4471-429d-9f1a-cb3b220cdb17',
        subject: 'Support Request 1',
        lastMessage: {
          content: 'Hello, I need help with my account.',
          date: new Date(),
        },
      },
      {
        id: '7b8e42ff-4471-429d-9f1a-cb3b220cdb18',
        subject: 'Support Request 2',
        lastMessage: {
          content: 'I have a question about my order.',
          date: new Date(),
        },
      },
      {
        id: '7b8e42ff-4471-429d-9f1a-cb3b220cdb18 ',
        subject: 'Support Request 3',
        lastMessage: {
          content: 'Can you assist me with a technical issue?',
          date: new Date(),
        },
      },
    ])
  }

  open(item: Conversation) {
    this.router.navigate(['/chats', item.id])
  }

  newConversation() {
    // TODO: logique d'ouverture/creation
    const id = 'conv-' + Math.floor(Math.random() * 1000)
    this.router.navigate(['/chats', id])
  }

  avatarLabel(item: Conversation): string {
    const t = item.subject || ''
    return t.slice(0, 2).toUpperCase()
  }
}
