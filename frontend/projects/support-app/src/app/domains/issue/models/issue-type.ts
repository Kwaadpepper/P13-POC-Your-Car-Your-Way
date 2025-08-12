import { Entity } from '~ycyw/shared'

import { IssueStatus } from '../enums'

import { ClientInfo } from './client-info-type'
import { ReservationInfo } from './reservation-info-type'

export interface Issue extends Entity {
  subject: string
  description: string
  status: IssueStatus
  client: ClientInfo
  reservation: ReservationInfo
  updatedAt: Date
}

export type IssueId = Issue['id']
