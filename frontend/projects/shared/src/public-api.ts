/*
 * Public API Surface of shared
 */

// * SCHEMAS
export {
  validationErrorSchema,
  type ValidationErrorZod,
} from './schemas'

// * LIB
export {
  AcrissCode,
  BadResponseError,
  checkServerReponse,
  retryMultipleTimes,
  SessionBroadcastService,
  SessionBroadcastType,
  SessionExpiredError,
  ValidationError,
  verifyResponseType,
  type DecodedAcrissInfo,
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
