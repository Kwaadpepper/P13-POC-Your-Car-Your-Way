package com.ycyw.shared.ddd.lib.event;

@FunctionalInterface
public interface DomainEventListener<T extends DomainEvent<?>> {
  void onEvent(T event);
}
