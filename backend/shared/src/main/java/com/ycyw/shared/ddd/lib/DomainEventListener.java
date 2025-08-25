package com.ycyw.shared.ddd.lib;

public interface DomainEventListener {
  void onEvent(Object event);
}
