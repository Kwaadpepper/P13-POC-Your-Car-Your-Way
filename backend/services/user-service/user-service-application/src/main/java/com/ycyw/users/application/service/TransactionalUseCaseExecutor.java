package com.ycyw.users.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;

import org.eclipse.jdt.annotation.Nullable;

@Service
public class TransactionalUseCaseExecutor implements UseCaseExecutor {

  @Override
  @Transactional
  public <I extends UseCaseInput, @Nullable O extends UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput) {
    return useCaseHandler.handle(usecaseInput);
  }
}
