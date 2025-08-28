package com.ycyw.support.application.service;

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

  public TransactionalUseCaseExecutor(DomainEventPublisher domainEventPublisher) {
    this.domainEventPublisher = domainEventPublisher;
  }

  @Override
  @Transactional
  public <I extends UseCaseInput, O extends @Nullable UseCaseOutput> O execute(
      UseCaseHandler<I, O> useCaseHandler, I usecaseInput) {
    try {
      O output = useCaseHandler.handle(usecaseInput);
      for (DomainEvent<?> event : eventsToPublish) {
        domainEventPublisher.publish(event);
      }
      eventsToPublish.clear();
      return output;
    } catch (Exception e) {
      eventsToPublish.clear();
      throw e;
    }
  }

  @Override
  public void publish(DomainEvent<?> event) {
    eventsToPublish.add(event);
    domainEventPublisher.publish(event);
  }
}
