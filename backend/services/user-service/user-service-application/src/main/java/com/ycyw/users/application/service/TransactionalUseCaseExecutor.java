package com.ycyw.users.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ycyw.shared.ddd.lib.UseCase;
import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.lib.UseCaseHandler;

@Service
public class TransactionalUseCaseExecutor implements UseCaseExecutor {

  @Override
  @Transactional
  public <I extends UseCase, O> O execute(UseCaseHandler<I, O> useCaseHandler, I usecase) {
    return useCaseHandler.execute(usecase);
  }
}
