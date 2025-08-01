import { ApplicationConfig } from '@ycyw/shared'
import { environment } from '../environments/environment'

export const AppConfig: ApplicationConfig = {
  appName: 'Your Car Your Way',
  version: '1.0.0',
  environment: environment.env,
}
