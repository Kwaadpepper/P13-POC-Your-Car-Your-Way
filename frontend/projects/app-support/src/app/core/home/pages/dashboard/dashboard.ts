import { Component, computed, inject, OnInit } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { DashboardViewModel } from './dashboard-viewmodel'

@Component({
  selector: 'support-dashboard',
  imports: [
    ButtonModule,
    RouterLink,
  ],
  providers: [
    DashboardViewModel,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private readonly viewModel = inject(DashboardViewModel)

  readonly appName = computed(() => this.viewModel.appName)
  readonly supportPhoneNumber = computed(() => this.viewModel.phoneNumber())
  readonly supportPhoneBusinessHours = computed(() => this.viewModel.phoneBusinessHours())

  readonly supportEmail = computed(() => this.viewModel.email())

  readonly supportChatBusinessHours = computed(() => this.viewModel.chatBusinessHours())

  ngOnInit(): void {
    this.viewModel.loadConfig()
  }
}
