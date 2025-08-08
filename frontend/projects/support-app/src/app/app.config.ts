import { ApplicationConfig, enableProdMode } from '@angular/core'
import { provideRouter, withComponentInputBinding } from '@angular/router'
import { APP_CONFIG, Configuration } from '@ycyw/shared'

import { environment } from '../environments/environment'

import { routes } from './app.routes'

export const configuration: Configuration = {
  ...{
    appName: 'Support App',
    version: '1.0.0',
  },
  environment: environment.env as Configuration['environment'],
}

const configProvider: ApplicationConfig['providers'] = []

if (environment.env === 'production') {
  enableProdMode()
  configProvider.push({
    provide: APP_CONFIG,
    useValue: configuration,
  })
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    ...configProvider,
  ],
}
