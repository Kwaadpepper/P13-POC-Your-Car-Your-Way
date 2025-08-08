export class SessionExpired extends Error {
  public constructor() {
    super('Session expired')
  }
}
