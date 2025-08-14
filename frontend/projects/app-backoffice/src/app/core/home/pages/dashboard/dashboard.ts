import { NgOptimizedImage, TitleCasePipe } from '@angular/common'
import { Component, computed, inject } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'
import { CardModule } from 'primeng/card'
import { DividerModule } from 'primeng/divider'
import { TagModule } from 'primeng/tag'

import { DashboardViewModel } from './dashboard-viewmodel'

@Component({
  selector: 'backoffice-dashboard',
  imports: [
    RouterLink,
    TitleCasePipe,
    NgOptimizedImage,
    ButtonModule,
    CardModule,
    DividerModule,
    TagModule,
  ],
  providers: [
    DashboardViewModel,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  private readonly viewModel = inject(DashboardViewModel)

  public readonly currentUserName = computed(() => this.viewModel.currentUser()?.name)
}
