import { initFederation } from '@angular-architects/native-federation'
import { enableProdMode } from '@angular/core'
import { environment } from '@env/environment'

if (environment.env === 'production') {
  enableProdMode()
}

initFederation('federation.manifest.json')
  .catch(err => console.error(err))
  .then(_ => import('./bootstrap'))
  .catch(err => console.error(err))
