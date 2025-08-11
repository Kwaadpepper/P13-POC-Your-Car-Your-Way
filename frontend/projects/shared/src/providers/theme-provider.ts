import { providePrimeNG } from 'primeng/config'

import { OpenClassrooms } from '../themes'

export const primeNgProvider = [
  providePrimeNG({
    ripple: false,
    theme: {
      preset: OpenClassrooms,
      options: {
        darkModeSelector: 'system',
        cssLayer: {
          name: 'primeng',
          order: 'tailwind-base, primeng, tailwind-utilities',
        },
      },
    },
  }),
]
