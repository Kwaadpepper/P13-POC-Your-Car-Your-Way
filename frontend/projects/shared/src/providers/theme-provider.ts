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
          order: 'theme,base,components,utilities,plugins,primeng',
        },
      },
    },
  }),
]
