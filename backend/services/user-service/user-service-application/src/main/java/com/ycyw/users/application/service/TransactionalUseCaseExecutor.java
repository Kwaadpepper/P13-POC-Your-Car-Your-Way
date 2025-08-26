package com.ycyw.users.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.lib.event.DomainEvent;
import com.ycyw.shared.ddd.lib.event.DomainEventPublisher;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class TransactionalUseCaseExecutor implements UseCaseExecutor {
  private DomainEventPublisher domainEventPublisher;

  private List<DomainEvent<?>> eventsToPublish = new ArrayList<>();

  public TransactionalUseCaseExecutor() {
    this.domainEventPublisher =
        (DomainEvent<?> event) -> {
          // No-op implementation
        };
  }

  @Override
  @Transactional
  public <I extends UseCaseInput, O extends @Nullable UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput) {
    return useCaseHandler.handle(usecaseInput);
  }

  @Override
  public void publish(DomainEvent<?> event) {
    eventsToPublish.add(event);
    domainEventPublisher.publish(event);
  }
}
