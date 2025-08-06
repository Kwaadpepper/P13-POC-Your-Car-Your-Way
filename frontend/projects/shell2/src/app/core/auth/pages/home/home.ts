import { Component, inject } from '@angular/core'
import { ButtonModule } from 'primeng/button'

import { HomeViewModel } from './home.viewmodel'

@Component({
  selector: 'shell2-home-page',
  imports: [
    ButtonModule,
  ],
  providers: [HomeViewModel],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomePage {
  public readonly viewModel = inject(HomeViewModel)
}
