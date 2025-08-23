package com.ycyw.support.application.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class TransactionalUseCaseExecutor implements UseCaseExecutor {

  @Override
  @Transactional
  public <I extends UseCaseInput, O extends @Nullable UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput) {
    return useCaseHandler.handle(usecaseInput);
  }
}
