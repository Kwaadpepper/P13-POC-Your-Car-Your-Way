import { Component, inject } from '@angular/core'
import { ActivatedRoute } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { SuperLabel } from '~support-domains/chat/components'
import { BackButton } from '~support-shared/components'

@Component({
  selector: 'support-chat-detail',
  imports: [
    ButtonModule,
    SuperLabel,
    BackButton,
  ],
  templateUrl: './chat-detail.html',
  styleUrl: './chat-detail.css',
})
export class ChatDetail {
  chatId = ''
  readonly route = inject(ActivatedRoute)

  constructor() {
    this.route.params.subscribe((params) => {
      this.chatId = params['id']
    })
  }
}
