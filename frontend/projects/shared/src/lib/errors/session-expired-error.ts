export class SessionExpiredError extends Error {
  public constructor() {
    super('Session expired')
  }
}
