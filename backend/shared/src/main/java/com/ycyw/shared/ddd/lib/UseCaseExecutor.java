package com.ycyw.shared.ddd.lib;

import org.eclipse.jdt.annotation.Nullable;

public interface UseCaseExecutor {
  <I extends UseCaseInput, O extends @Nullable UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput);
}
