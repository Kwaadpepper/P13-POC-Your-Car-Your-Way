import { Component } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { BackButton } from '~support-shared/components'

@Component({
  selector: 'support-chat-window',
  imports: [
    ButtonModule,
    RouterLink,
    BackButton,
  ],
  templateUrl: './chat-window.html',
  styleUrl: './chat-window.css',
})
export class ChatWindow {

}
