package com.ycyw.support.application.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.DomainEvent;
import com.ycyw.shared.ddd.lib.DomainEventPublisher;
import com.ycyw.support.application.lib.EventSerializationUtils;

@Component
public class RabbitMqDomainEventPublisher implements DomainEventPublisher {
  private final RabbitTemplate rabbitTemplate;
  private final String exchange;

  public RabbitMqDomainEventPublisher(
      RabbitTemplate rabbitTemplate,
      @Value("${eventbus.rabbitmq.exchange:support-chat-exchange}") String exchange) {
    this.rabbitTemplate = rabbitTemplate;
    this.exchange = exchange;
  }

  @Override
  public void publish(DomainEvent<?> event) {
    String routingKey = "support-chat." + event.routingKey();
    String payloadJson = EventSerializationUtils.serialize(event);
    rabbitTemplate.convertAndSend(exchange, routingKey, payloadJson);
  }
}
