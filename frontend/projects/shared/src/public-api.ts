/*
 * Public API Surface of shared
 */

// * CONSTANTS
export {
  SESSION_USER_STORAGE_KEY,
} from './lib'

// * SCHEMAS
export {
  uuidSchema,
  validationErrorSchema,
  type UUIDZod,
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
  type Repository,
  type SessionBroadcastMessage,
  type SessionSnapshot,
  type SessionStoreReadonly,
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
