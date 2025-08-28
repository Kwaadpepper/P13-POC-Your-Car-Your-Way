package com.ycyw.shared.ddd.lib.event;

import java.util.function.Consumer;

public interface AggregateEventBus<E> extends Consumer<E> {
  public default void publish(E event) {
    accept(event);
  }
}
