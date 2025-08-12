import { Component, computed, inject } from '@angular/core'
import { RouterLink } from '@angular/router'

import { ButtonModule } from 'primeng/button'

import { BusinessHours, DashboardViewModel } from './dashboard-viewmodel'

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
export class Dashboard {
  private readonly viewModel = inject(DashboardViewModel)

  readonly appName = computed(() => this.viewModel.appName)
  readonly supportPhoneNumber = computed(() => this.viewModel.supportContact().phone.number)
  readonly supportPhoneBusinessHours = computed(() => this.mapBusinessHours(
    this.viewModel
      .supportContact()
      .phone
      .businessHours,
  ))

  readonly supportChatBusinessHours = computed(() => this.mapBusinessHours(
    this.viewModel
      .supportContact()
      .chat
      .businessHours,
  ))

  readonly supportEmail = computed(() => this.viewModel.supportContact().email)

  private readonly knownDatesForWeekdays = {
    monday: new Date('2025-08-04T12:00:00Z'),
    tuesday: new Date('2025-08-05T12:00:00Z'),
    wednesday: new Date('2025-08-06T12:00:00Z'),
    thursday: new Date('2025-08-07T12:00:00Z'),
    friday: new Date('2025-08-08T12:00:00Z'),
    saturday: new Date('2025-08-09T12:00:00Z'),
    sunday: new Date('2025-08-10T12:00:00Z'),
  }

  private mapBusinessHours(businessHours: BusinessHours) {
    return Object.entries(businessHours).map(([day, hours]) => ({
      day: this.translateWeekday(day as keyof typeof this.knownDatesForWeekdays),
      from: hours.from,
      to: hours.to,
    }))
  }

  private translateWeekday(englishDay: keyof typeof this.knownDatesForWeekdays): string {
    const formatter = new Intl.DateTimeFormat(navigator.language, { weekday: 'long' })

    return formatter.format(
      this.knownDatesForWeekdays[englishDay],
    )
  }
}
