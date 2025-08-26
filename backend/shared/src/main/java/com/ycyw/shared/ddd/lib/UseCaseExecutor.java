package com.ycyw.shared.ddd.lib;

import com.ycyw.shared.ddd.lib.event.DomainEvent;

import org.eclipse.jdt.annotation.Nullable;

public interface UseCaseExecutor {
  <I extends UseCaseInput, O extends @Nullable UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput);

  void publish(DomainEvent<?> event);
}
