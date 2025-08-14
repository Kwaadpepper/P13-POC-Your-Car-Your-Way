import { NgOptimizedImage } from '@angular/common'
import { Component, inject } from '@angular/core'
import { RouterModule } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { HomeViewModel } from './home.viewmodel'

@Component({
  selector: 'shell-home-page',
  imports: [
    RouterModule,
    ButtonModule,
    NgOptimizedImage,
  ],
  providers: [HomeViewModel],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomePage {
  public readonly viewModel = inject(HomeViewModel)
}
