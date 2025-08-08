import { Component } from '@angular/core'
import { RouterLink } from '@angular/router'
import { BackButton } from '@support-shared/components'
import { ButtonModule } from 'primeng/button'

@Component({
  selector: 'support-tchat-window',
  imports: [
    ButtonModule,
    RouterLink,
    BackButton,
  ],
  templateUrl: './tchat-window.html',
  styleUrl: './tchat-window.css',
})
export class TchatWindow {

}
