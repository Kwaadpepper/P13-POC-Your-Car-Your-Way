export interface ClientInfo {
  first_name: string
  last_name: string
  email: string
  phone: string
  birthdate: Date
  address: {
    line1: string
    line2?: string
    line3?: string
    city: string
    post_code: string
    country: string
  }
}
