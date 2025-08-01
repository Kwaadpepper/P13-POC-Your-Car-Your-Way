import { Component, inject } from '@angular/core'
import { RouterLink } from '@angular/router'
import { ButtonModule } from 'primeng/button'

import { HomeViewModel } from './home.viewmodel'

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    ButtonModule,
  ],
  providers: [HomeViewModel],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  public readonly viewModel = inject(HomeViewModel)
}
