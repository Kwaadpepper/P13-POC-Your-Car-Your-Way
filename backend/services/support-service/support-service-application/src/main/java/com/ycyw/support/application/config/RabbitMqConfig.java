package com.ycyw.support.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
  private final String exchange;
  private final String queueName;

  public RabbitMqConfig(
      @Value("${eventbus.rabbitmq.exchange:support-chat-exchange}") String exchange,
      @Value("${eventbus.rabbitmq.queue:support-chat-queue}") String queueName) {
    this.exchange = exchange;
    this.queueName = queueName;
  }

  public String getExchange() {
    return exchange;
  }

  public String getQueueName() {
    return queueName;
  }

  @Bean
  TopicExchange chatExchange() {
    return new TopicExchange(exchange);
  }

  @Bean
  Queue chatQueue() {
    return new Queue(queueName);
  }

  @Bean
  Binding chatBinding(Queue chatQueue, TopicExchange chatExchange) {
    return BindingBuilder.bind(chatQueue).to(chatExchange).with("support-chat.#");
  }
}
