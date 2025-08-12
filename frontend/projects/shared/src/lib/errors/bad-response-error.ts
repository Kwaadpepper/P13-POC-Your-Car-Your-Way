export class BadResponseError extends Error {
  public override name = 'BadResponse'

  constructor(cause?: unknown) {
    super('Bad response from server', { cause: cause })
  }
}
