import { InjectionToken } from '@angular/core'

import { Configuration } from '~ycyw/shared'

import { environment } from '@ycyw/support-env/environment'

import config from '../../../application.json'

export const configuration: Configuration = {
  ...config,
  environment: environment.env as Configuration['environment'],
}

export const APP_CONFIG = new InjectionToken<Configuration>('support.config', {
  providedIn: 'root',
  factory: () => configuration,
})
