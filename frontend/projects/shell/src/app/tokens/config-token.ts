import { InjectionToken } from '@angular/core'

import { environment } from '~shell-env/environment'
import { Configuration } from '~ycyw/shared'

import config from '../../../application.json'

export const configuration: Configuration = {
  ...config,
  environment: environment.env as Configuration['environment'],
}

export const APP_CONFIG = new InjectionToken<Configuration>('shell.config', {
  providedIn: 'root',
  factory: () => configuration,
})
