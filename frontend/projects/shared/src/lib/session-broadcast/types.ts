import { UUID } from '../../types'

export const SESSION_BROADCAST_CHANNEL = 'YCYW_SESSION_BROADCAST'

export enum SessionBroadcastType {
  LOGIN = 'login',
  LOGOUT = 'logout',
  REFRESH = 'refresh',
}

export interface LoginEvent {
  user: SharedUserProfile
}

export type LogoutEvent = Readonly<Record<never, never>>
export type RefreshEvent = Readonly<Record<never, never>>

export type SessionBroadcastMessage
  = | { type: SessionBroadcastType.LOGIN, payload: LoginEvent, sourceId: string, timestamp: number }
    | { type: SessionBroadcastType.LOGOUT, payload: LogoutEvent, sourceId: string, timestamp: number }
    | { type: SessionBroadcastType.REFRESH, payload: RefreshEvent, sourceId: string, timestamp: number }

export interface SharedUserProfile {
  readonly id: UUID
  readonly name: string
  readonly role: string
  readonly email: string
}
