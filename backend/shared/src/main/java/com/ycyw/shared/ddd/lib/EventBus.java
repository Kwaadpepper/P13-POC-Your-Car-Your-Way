package com.ycyw.shared.ddd.lib;

public interface EventBus {
  void publish(DomainEvent<?> event);

  void close();

  boolean isOpen();
}
