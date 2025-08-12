import { inject, Injectable, signal } from '@angular/core'

import { APP_CONFIG } from '~support-tokens/config-token'

export type BusinessHours = Record<string, {
  from: string
  to: string
}>

@Injectable({
  providedIn: 'root',
})
export class DashboardViewModel {
  private readonly config = inject(APP_CONFIG)

  // TODO: IMPORT Support Config

  public readonly supportContact = signal({
    phone: {
      number: '+33823455689',
      businessHours: {
        monday: {
          from: '09:00', to: '18:00',
        },
        tuesday: {
          from: '09:00', to: '18:00',
        },
        wednesday: {
          from: '09:00', to: '18:00',
        },
        thursday: {
          from: '09:00', to: '18:00',
        },
        friday: {
          from: '09:00', to: '18:00',
        },
      },
    },
    chat: {
      businessHours: {
        monday: {
          from: '08:00', to: '19:00',
        },
        tuesday: {
          from: '08:00', to: '19:00',
        },
        wednesday: {
          from: '08:00', to: '19:00',
        },
        thursday: {
          from: '08:00', to: '19:00',
        },
        friday: {
          from: '08:00', to: '19:00',
        },
      },
    },
    email: 'support@example.net',
    address: {
      line1: '123 rue de la Paix',
      line2: 'Rez-de-chaussée',
      line3: 'Bâtiment A',
      city: 'Paris',
      zip: '75001',
      country: 'France',
    },
  }).asReadonly()

  get appName(): string {
    return this.config.appName
  }
}
