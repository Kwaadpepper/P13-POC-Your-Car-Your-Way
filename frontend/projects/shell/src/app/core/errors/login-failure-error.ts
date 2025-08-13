export class LoginFailureError extends Error {
  public constructor() {
    super('Bad credentials')
  }
}
