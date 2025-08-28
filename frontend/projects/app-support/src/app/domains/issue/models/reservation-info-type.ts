import { AcrissCode } from '@ycyw/shared'

interface AgencyInfo {
  label: string
  phone: string
  email: string
  address: {
    line1: string
    line2?: string
    line3?: string
    city: string
    zipCode: string
    country: string
  }
  coordinates: {
    lat: number
    long: number
  }
}

interface VehicleInfo {
  category: AcrissCode
}

export interface ReservationInfo {
  status: string
  from: {
    agency: AgencyInfo
    at: Date
  }
  to: {
    agency: AgencyInfo
    at: Date
  }
  vehicle: VehicleInfo
  payment: string
}
