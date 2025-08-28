import { computed, inject, Injectable, OnDestroy, resource } from '@angular/core'

import { firstValueFrom, Subscription } from 'rxjs'

import { SUPPORT_CONFIG_SERVICE } from '@ycyw/support-tokens/support-config-service-token'

@Injectable({
  providedIn: 'root',
  deps: [SUPPORT_CONFIG_SERVICE],
})
export class SupportConfigStore implements OnDestroy {
  private readonly service = inject(SUPPORT_CONFIG_SERVICE)
  private readonly _supportConfig = resource({
    defaultValue: {
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
    },
    loader: this.loadSupportConfig.bind(this),
  })

  private readonly supportConfig = this._supportConfig.value.asReadonly()

  readonly loading = computed(() => this._supportConfig.isLoading())
  readonly error = computed(() => this._supportConfig.error())

  readonly phoneNumber = computed(() => this.supportConfig().phone.number)
  readonly email = computed(() => this.supportConfig().email)
  readonly address = computed(() => this.supportConfig().address)

  readonly chatBusinessHours = computed(() => this.supportConfig().chat.businessHours ?? {})
  readonly phoneBusinessHours = computed(() => this.supportConfig().phone.businessHours ?? {})

  private readonly sub: Subscription

  constructor() {
    this.sub = this.service.getConfig().subscribe({
      next: config => this._supportConfig.set(config),
    })
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe()
  }

  reload() {
    this._supportConfig.reload()
  }

  private async loadSupportConfig() {
    return await firstValueFrom(this.service.getConfig())
  }
}
