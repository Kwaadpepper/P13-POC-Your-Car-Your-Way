import { Component, inject } from '@angular/core'
import { ActivatedRoute } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { SuperLabel } from '~support-domains/tchat/components'
import { BackButton } from '~support-shared/components'

@Component({
  selector: 'support-tchat-detail',
  imports: [
    ButtonModule,
    SuperLabel,
    BackButton,
  ],
  templateUrl: './tchat-detail.html',
  styleUrl: './tchat-detail.css',
})
export class TchatDetail {
  chatId = ''
  readonly route = inject(ActivatedRoute)

  constructor() {
    this.route.params.subscribe((params) => {
      this.chatId = params['id']
    })
  }
}
