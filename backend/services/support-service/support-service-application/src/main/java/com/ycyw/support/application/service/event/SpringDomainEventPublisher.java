package com.ycyw.support.application.service.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.event.DomainEvent;
import com.ycyw.shared.ddd.lib.event.DomainEventPublisher;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public void publish(DomainEvent<?> event) {
    this.applicationEventPublisher.publishEvent(event);
  }
}
