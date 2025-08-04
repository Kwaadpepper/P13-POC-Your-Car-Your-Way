import { Component, inject, OnInit, ViewContainerRef } from '@angular/core'
import { RouterLink } from '@angular/router'
import { ButtonModule } from 'primeng/button'

import { TchatWindow } from '@remote/users/components'
import { HomeViewModel } from './home.viewmodel'

@Component({
  selector: 'app-home',
  imports: [
    RouterLink,
    ButtonModule,
    TchatWindow,
  ],
  providers: [HomeViewModel],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  public readonly viewModel = inject(HomeViewModel)
  private readonly viewContainer = inject(ViewContainerRef)

  ngOnInit(): void {
    console.log('HomeComponent initialized')
  }
}
