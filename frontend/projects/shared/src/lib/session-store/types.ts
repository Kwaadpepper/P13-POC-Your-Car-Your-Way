import { SharedUserProfile } from '../session-broadcast/types'

export interface SessionSnapshot {
  isLoggedIn: boolean
  user: SharedUserProfile | null
}
