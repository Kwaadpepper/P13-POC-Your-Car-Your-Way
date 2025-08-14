type D = '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
type HH = `${'0' | '1'}${D}` | `2${'0' | '1' | '2' | '3'}`
type MM = `${'0' | '1' | '2' | '3' | '4' | '5'}${D}`
type Hour = `${HH}:${MM}`

type WeekDay
  = | 'monday'
    | 'tuesday'
    | 'wednesday'
    | 'thursday'
    | 'friday'
    | 'saturday'
    | 'sunday'

interface TimeRange {
  from: Hour
  to: Hour
}

type BusinessHours = Partial<Record<WeekDay, TimeRange>>

type E164 = `+${string}`

export interface SupportConfig {
  phone: {
    number: E164
    businessHours: BusinessHours
  }
  chat: {
    businessHours: BusinessHours
  }
  email: string
  address: {
    line1: string
    line2?: string
    line3?: string
    city: string
    zip: string
    country: string
  }
}
