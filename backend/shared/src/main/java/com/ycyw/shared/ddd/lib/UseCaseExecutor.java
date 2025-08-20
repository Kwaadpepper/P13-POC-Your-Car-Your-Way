package com.ycyw.shared.ddd.lib;

import org.eclipse.jdt.annotation.Nullable;

public interface UseCaseExecutor {
  <I extends UseCaseInput, @Nullable O extends UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput);
}
