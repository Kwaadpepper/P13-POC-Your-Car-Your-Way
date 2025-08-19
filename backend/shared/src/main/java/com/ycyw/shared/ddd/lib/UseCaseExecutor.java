package com.ycyw.shared.ddd.lib;

public interface UseCaseExecutor {
  <I extends UseCase, O> O execute(UseCaseHandler<I, O> useCaseHandler, I usecase);
}
