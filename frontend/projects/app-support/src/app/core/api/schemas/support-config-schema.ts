import { z } from 'zod'

type D = '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
type HH = `${'0' | '1'}${D}` | `2${'0' | '1' | '2' | '3'}`
type MM = `${'0' | '1' | '2' | '3' | '4' | '5'}${D}`
type Hour = `${HH}:${MM}`

const hourSchema = z
  .string()
  .regex(/^([01]\d|2[0-3]):[0-5]\d$/, 'Format attendu HH:MM (00:00–23:59)')
  .transform(value => value as Hour)

const toMinutes = (t: string) => {
  const [h, m] = t.split(':').map(Number)
  return h * 60 + m
}

const timeRangeSchema = z
  .object({
    from: hourSchema,
    to: hourSchema,
  })
  .refine(
    ({ from, to }) => toMinutes(from) < toMinutes(to),
    { message: 'L\'heure de début doit être antérieure à l\'heure de fin', path: ['to'] },
  )

const businessHoursSchema = z
  .object({
    monday: timeRangeSchema.optional(),
    tuesday: timeRangeSchema.optional(),
    wednesday: timeRangeSchema.optional(),
    thursday: timeRangeSchema.optional(),
    friday: timeRangeSchema.optional(),
    saturday: timeRangeSchema.optional(),
    sunday: timeRangeSchema.optional(),
  })
  .strict()

const addressSchema = z.object({
  line1: z.string().min(1),
  line2: z.string().optional().nullable().transform(val => val === null ? undefined : val),
  line3: z.string().optional().nullable().transform(val => val === null ? undefined : val),
  city: z.string().min(1),
  zip: z.string().min(1),
  country: z.string().min(1),
})

const phoneNumberSchema = z
  .string()
  .regex(/^\+\d{6,15}$/, 'Numéro invalide (attendu: +########)')
  .transform(value => value as `+${string}`)

export const supportConfigSchema = z.object({
  phone: z.object({
    number: phoneNumberSchema,
    businessHours: businessHoursSchema,
  }),
  chat: z.object({
    businessHours: businessHoursSchema,
  }),
  email: z.string().email(),
  address: addressSchema,
})

export type SupportConfigZod = z.infer<typeof supportConfigSchema>
