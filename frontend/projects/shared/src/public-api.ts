/*
 * Public API Surface of shared
 */

// * LIB
export {
  SessionBroadcastService,
  SessionBroadcastType,
  type LoginEvent,
  type LogoutEvent,
  type RefreshEvent,
  type SharedUserProfile,
} from './lib'

// * CONFIG
export {
  type Configuration,
} from './config'

// * PROVIDERS
export {
  primeNgProvider,
} from './providers'

// * TYPES
export type {
  Entity,
  PageOf,
  UUID,
  zSchema,
} from './types'
