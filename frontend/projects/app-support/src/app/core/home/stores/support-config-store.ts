import { Injectable, computed, inject, signal } from '@angular/core'

import { firstValueFrom } from 'rxjs'

import { SupportConfig } from '@ycyw/support-domains/support/dtos'
import { SUPPORT_CONFIG_SERVICE } from '@ycyw/support-tokens/support-config-service-token'

@Injectable({
  providedIn: 'root',
  deps: [SUPPORT_CONFIG_SERVICE],
})
export class SupportConfigStore {
  private readonly service = inject(SUPPORT_CONFIG_SERVICE)
  private readonly _supportConfig = signal<SupportConfig>({
    phone: {
      number: '+0000000000',
      businessHours: {},
    },
    chat: {
      businessHours: {},
    },
    email: '',
    address: {
      line1: '',
      line2: undefined,
      line3: undefined,
      city: '',
      zip: '',
      country: '',
    },
  })

  private readonly supportConfig = this._supportConfig.asReadonly()

  readonly phoneNumber = computed(() => this.supportConfig().phone.number)
  readonly email = computed(() => this.supportConfig().email)
  readonly address = computed(() => this.supportConfig().address)

  readonly chatBusinessHours = computed(() => this.supportConfig().chat.businessHours ?? {})
  readonly phoneBusinessHours = computed(() => this.supportConfig().phone.businessHours ?? {})

  reloadAll() {
    this.loadSupportConfig().then((config) => {
      this._supportConfig.set(config)
    })
  }

  private async loadSupportConfig() {
    return await firstValueFrom(this.service.getConfig())
  }
}
