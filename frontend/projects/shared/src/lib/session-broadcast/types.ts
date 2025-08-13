import { UUID } from '../../types'

export const SESSION_BROADCAST_CHANNEL = 'YCYW_SESSION_BROADCAST'

export enum SessionBroadcastType {
  LOGIN = 'login',
  LOGOUT = 'logout',
  REFRESH = 'refresh',
}

export interface LoginEvent {
  type: SessionBroadcastType.LOGIN
  user: SharedUserProfile
}

export interface LogoutEvent {
  type: SessionBroadcastType.LOGOUT
}

export interface RefreshEvent {
  type: SessionBroadcastType.REFRESH
}

export type SessionBroadcastEvent = LoginEvent | LogoutEvent | RefreshEvent

export interface SessionBroadcastMessage<T> {
  type: SessionBroadcastType
  payload?: T
  sourceId: string
  timestamp: number
}

export interface SharedUserProfile {
  id: UUID
  name: string
  email: string
}
