import { computed, inject, Injectable } from '@angular/core'

import { SupportConfigStore } from '@ycyw/support-core/home/stores'
import { SupportConfig } from '@ycyw/support-domains/support/dtos'
import { APP_CONFIG } from '@ycyw/support-tokens/config-token'

export type BusinessHours = Record<string, {
  from: string
  to: string
}>

@Injectable({
  providedIn: 'root',
})
export class DashboardViewModel {
  private readonly config = inject(APP_CONFIG)
  private readonly supportConfigStore = inject(SupportConfigStore)

  readonly phoneNumber = computed<string>(() => this.supportConfigStore.phoneNumber())
  readonly email = computed<string>(() => this.supportConfigStore.email())
  readonly address = computed<SupportConfig['address']>(() => this.supportConfigStore.address())

  readonly chatBusinessHours = computed(() =>
    this.mapBusinessHours(
      this.supportConfigStore.chatBusinessHours(),
    ))

  readonly phoneBusinessHours = computed(() =>
    this.mapBusinessHours(
      this.supportConfigStore.phoneBusinessHours(),
    ))

  get appName(): string {
    return this.config.appName
  }

  private readonly knownDatesForWeekdays = {
    monday: new Date('2025-08-04T12:00:00Z'),
    tuesday: new Date('2025-08-05T12:00:00Z'),
    wednesday: new Date('2025-08-06T12:00:00Z'),
    thursday: new Date('2025-08-07T12:00:00Z'),
    friday: new Date('2025-08-08T12:00:00Z'),
    saturday: new Date('2025-08-09T12:00:00Z'),
    sunday: new Date('2025-08-10T12:00:00Z'),
  }

  public loadConfig() {
    this.supportConfigStore.reloadAll()
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
