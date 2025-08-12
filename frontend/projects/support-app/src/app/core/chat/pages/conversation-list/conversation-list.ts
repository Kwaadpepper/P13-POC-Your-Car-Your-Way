import { DatePipe } from '@angular/common'
import { Component, inject, OnInit, signal } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'

import { AvatarModule } from 'primeng/avatar'
import { BadgeModule } from 'primeng/badge'
import { ButtonModule } from 'primeng/button'
import { ListboxModule } from 'primeng/listbox'
import { MessageModule } from 'primeng/message'

import { Conversation } from '~support-domains/chat/models'
import { BackButton } from '~support-shared/components'

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
  templateUrl: './conversation-list.html',
  styleUrl: './conversation-list.css',
})
export class ConversationList implements OnInit {
  private readonly router = inject(Router)
  private readonly route = inject(ActivatedRoute)

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
    console.log(this.route.snapshot)
    this.router.navigate(['.', item.id], { relativeTo: this.route })
  }

  newConversation() {
    // TODO: logique d'ouverture/creation
    const id = 'conv-' + Math.floor(Math.random() * 1000)
    this.router.navigate([id], { relativeTo: this.route })
  }

  avatarLabel(item: Conversation): string {
    const t = item.subject || ''
    return t.slice(0, 2).toUpperCase()
  }
}
