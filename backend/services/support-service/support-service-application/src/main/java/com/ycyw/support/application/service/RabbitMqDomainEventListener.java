package com.ycyw.support.application.service;

import java.nio.charset.StandardCharsets;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.DomainEvent;
import com.ycyw.shared.ddd.lib.DomainEventListener;
import com.ycyw.support.application.lib.EventSerializationUtils;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitMqDomainEventListener implements DomainEventListener {
  private static final Logger logger = LoggerFactory.getLogger(RabbitMqDomainEventListener.class);

  @RabbitListener(queues = "${eventbus.rabbitmq.queue:support-chat-queue}")
  @Override
  public void onEvent(Object event) {
    @Nullable final Object receivedEvent = event;

    final String payload;
    if (receivedEvent instanceof String s) {
      payload = s;
    } else if (receivedEvent instanceof byte[] bytes) {
      payload = new String(bytes, StandardCharsets.UTF_8);
    } else if (receivedEvent != null) {
      payload = receivedEvent.toString();
    } else {
      logger.warn("Received null event");
      return;
    }

    final var eventObject = EventSerializationUtils.deserialize(payload, DomainEvent.class);

    logger.info("Received event: {}", eventObject);
  }
}
