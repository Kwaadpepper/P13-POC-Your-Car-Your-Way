import { Entity } from '~ycyw/shared'

export type FaqType = string

export interface Faq extends Entity {
  id: string
  question: string
  answer: string
  type: FaqType
}
