import { InjectionToken } from '@angular/core'

export interface Configuration {
  appName: string
  version: string
  environment: 'development' | 'production' | 'test'
}

export const APP_CONFIG = new InjectionToken<Configuration>('app.config')
