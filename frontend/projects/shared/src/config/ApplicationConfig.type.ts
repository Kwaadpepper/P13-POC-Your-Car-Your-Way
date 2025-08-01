export interface ApplicationConfig {
  appName: string
  version: string
  environment: 'development' | 'production' | 'test'
}
