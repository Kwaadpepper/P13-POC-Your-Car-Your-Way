import { Component } from '@angular/core'
import { RouterLink } from '@angular/router'
import { BackButton } from '@support-shared/components'
import { ButtonModule } from 'primeng/button'

@Component({
  selector: 'support-dashboard',
  imports: [
    ButtonModule,
    RouterLink,
    BackButton,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

}
