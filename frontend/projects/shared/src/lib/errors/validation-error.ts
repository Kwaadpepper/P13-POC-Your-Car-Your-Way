export class ValidationError extends Error {
  private readonly errors: Map<string, string>

  public constructor(errors: Record<string, string>) {
    super('Validation error')
    this.errors = new Map(Object.entries(errors))
  }

  public getErrors(): Map<string, string> {
    return new Map(this.errors)
  }
}
