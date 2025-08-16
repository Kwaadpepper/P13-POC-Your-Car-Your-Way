import { z } from 'zod'

import { AcrissCode, uuidSchema } from '@ycyw/shared'
import { IssueStatus } from '@ycyw/support-domains/issue/enums'

const addressSchema = z.object({
  line1: z.string().nonempty(),
  line2: z.string().optional(),
  line3: z.string().optional(),
  city: z.string().nonempty(),
  post_code: z.string().nonempty(),
  country: z.string().nonempty(),
})

const clientInfoSchema = z.object({
  first_name: z.string().nonempty(),
  last_name: z.string().nonempty(),
  email: z.string().email(),
  phone: z.string().min(3),
  birthdate: z.coerce.date(),
  address: addressSchema,
})

const agencyInfoSchema = z.object({
  label: z.string().nonempty(),
  phone: z.string().min(3),
  email: z.string().email(),
  address: addressSchema,
  coordinates: z.object({
    lat: z.number().refine(v => Math.abs(v) <= 90, 'Latitude invalide'),
    long: z.number().refine(v => Math.abs(v) <= 180, 'Longitude invalide'),
  }),
})

const vehicleInfoSchema = z.object({
  category: z.string().transform((code, ctx) => {
    try {
      return AcrissCode.fromCode(code.toUpperCase())
    }
    catch {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: `Code ACRISS invalide: ${code}`,
      })
      return z.NEVER
    }
  }),
})

const reservationInfoSchema = z.object({
  status: z.string().nonempty(),
  from: z.object({
    agency: agencyInfoSchema,
    at: z.coerce.date(),
  }),
  to: z.object({
    agency: agencyInfoSchema,
    at: z.coerce.date(),
  }),
  vehicle: vehicleInfoSchema,
  payment: z.string().nonempty(),
})

export const issueSchema = z.object({
  id: uuidSchema,
  subject: z.string().nonempty(),
  answer: z.string().nullable(),
  description: z.string().nonempty(),
  status: z.nativeEnum(IssueStatus),
  client: clientInfoSchema,
  reservation: reservationInfoSchema,
  conversation: uuidSchema.nullable(),
  updatedAt: z.coerce.date(),
})

export type IssueZod = z.infer<typeof issueSchema>
