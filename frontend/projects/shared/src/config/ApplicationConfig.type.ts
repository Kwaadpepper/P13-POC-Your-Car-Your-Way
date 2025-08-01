import { InjectionToken } from '@angular/core'

export interface ApplicationConfig {
  appName: string
  version: string
  environment: 'development' | 'production' | 'test'
}

export const APP_CONFIG = new InjectionToken<ApplicationConfig>('app.config')
