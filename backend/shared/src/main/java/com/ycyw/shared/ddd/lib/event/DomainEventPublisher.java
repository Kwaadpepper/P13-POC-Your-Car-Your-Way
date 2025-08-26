package com.ycyw.shared.ddd.lib.event;

@FunctionalInterface
public interface DomainEventPublisher {
  void publish(DomainEvent<?> event);
}
