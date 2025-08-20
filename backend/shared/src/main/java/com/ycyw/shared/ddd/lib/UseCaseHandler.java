package com.ycyw.shared.ddd.lib;

import org.eclipse.jdt.annotation.Nullable;

public interface UseCaseHandler<I extends UseCaseInput, O extends @Nullable UseCaseOutput> {
  /**
   * Executes the use case with the provided input.
   *
   * @param usecaseInput the input for the use case
   * @return the output of the use case
   */
  @Nullable O execute(I usecaseInput);
}
