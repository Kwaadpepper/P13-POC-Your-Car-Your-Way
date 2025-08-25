package com.ycyw.shared.ddd.lib;

public interface DomainEventPublisher {
  void publish(DomainEvent<?> event);
}
