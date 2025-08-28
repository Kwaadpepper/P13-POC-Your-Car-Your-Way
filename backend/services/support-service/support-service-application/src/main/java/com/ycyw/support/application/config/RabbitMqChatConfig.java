package com.ycyw.support.application.config;

import java.util.UUID;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqChatConfig {
  private final String exchange;
  private final String newMessageQueue;

  public RabbitMqChatConfig(@Value("${rabbitmq.chat.exchange}") String exchange) {
    this.exchange = exchange;
    this.newMessageQueue = "support-new-message-queue-" + UUID.randomUUID();
  }

  public String getExchange() {
    return exchange;
  }

  public String getNewMessageQueue() {
    return newMessageQueue;
  }

  @Bean
  FanoutExchange supportMessageExchange() {
    return new FanoutExchange(this.exchange);
  }

  @Bean
  Queue supportNewMessageQueue() {
    return new Queue(this.newMessageQueue, true, false, true);
  }

  @Bean
  Binding binding() {
    return BindingBuilder.bind(supportNewMessageQueue()).to(supportMessageExchange());
  }
}
