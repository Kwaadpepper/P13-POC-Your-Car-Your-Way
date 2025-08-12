import { catchError, map, Observable, pipe, throwError, UnaryFunction } from 'rxjs'
import { z, ZodError } from 'zod'

import { BadResponseError } from '../errors'

/**
 * Verify the response type.
 * @param zodObj  The zod object to verify the response.
 * @returns The observable.
 */
export function verifyResponseType<T extends z.ZodTypeAny>(zodObj: T): UnaryFunction<Observable<unknown>, Observable<z.infer<T>>> {
  return pipe(
    map((response) => {
      return zodObj.parse(response)
    }),
    catchError((error) => {
      if (error instanceof ZodError) {
        return throwError(() => new BadResponseError(error))
      }
      return throwError(() => error)
    }),
  )
}
