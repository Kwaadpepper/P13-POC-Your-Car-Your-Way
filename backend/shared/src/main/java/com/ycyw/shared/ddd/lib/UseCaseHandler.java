package com.ycyw.shared.ddd.lib;

public interface UseCaseHandler<I extends UseCase, O> {
  /**
   * Executes the use case with the provided input.
   *
   * @param usecase the input for the use case
   * @return the output of the use case
   */
  O execute(I usecase);
}
