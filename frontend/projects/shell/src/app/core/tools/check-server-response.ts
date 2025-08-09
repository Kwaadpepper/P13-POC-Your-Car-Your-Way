import { HttpErrorResponse } from '@angular/common/http'

import { catchError, Observable, pipe, throwError, UnaryFunction } from 'rxjs'

import { validationErrorSchema } from '~shell-core/auth/api/schemas'
import { ValidationError } from '~shell-core/errors'

/**
 * Check the server response and throw an error if the response is not valid.
 * @returns The observable.
 */
export function checkServerReponse<T>(): UnaryFunction<Observable<T>, Observable<T>> {
  return pipe(
    catchError((error) => {
      if (!(error instanceof HttpErrorResponse)) {
        return throwError(() => error)
      }
      if (error.status === 422) {
        const validationError = validationErrorSchema.parse(error.error)
        throw new ValidationError(validationError.errors)
      }
      return throwError(() => error)
    }),
  )
}
