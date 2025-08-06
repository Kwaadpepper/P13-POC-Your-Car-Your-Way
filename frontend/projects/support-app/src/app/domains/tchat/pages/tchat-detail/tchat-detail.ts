import { Component, inject } from '@angular/core'
import { ActivatedRoute, RouterLink } from '@angular/router'

import { SuperLabel } from '@support-domains/tchat/components'

@Component({
  selector: 'app-tchat-detail',
  imports: [
    RouterLink,
    SuperLabel,
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
