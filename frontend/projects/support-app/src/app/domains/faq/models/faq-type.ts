import { Entity } from '~ycyw/shared'

export type FaqType = string

export interface Faq extends Entity {
  question: string
  answer: string
  category: FaqType
}

export type FaqId = Faq['id']
