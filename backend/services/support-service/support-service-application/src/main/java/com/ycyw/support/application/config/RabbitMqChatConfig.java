package com.ycyw.support.application.config;

import java.util.UUID;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqChatConfig {
  private final String exchange;
  private final String newMessageQueue;
  private final String presenceQueue;
  private final String typingQueue;

  public RabbitMqChatConfig(@Value("${rabbitmq.chat.exchange}") String exchange) {
    this.exchange = exchange;
    this.newMessageQueue = "support-new-message-queue-" + UUID.randomUUID();
    this.presenceQueue = "support-presence-queue-" + UUID.randomUUID();
    this.typingQueue = "support-typing-queue-" + UUID.randomUUID();
  }

  public String getExchange() {
    return exchange;
  }

  public String getNewMessageQueue() {
    return newMessageQueue;
  }

  public String getPresenceQueue() {
    return presenceQueue;
  }

  public String getTypingQueue() {
    return typingQueue;
  }

  @Bean
  TopicExchange supportMessageExchange() {
    return new TopicExchange(this.exchange, true, true);
  }

  @Bean
  Queue supportNewMessageQueue() {
    return new Queue(this.newMessageQueue, true, true, true);
  }

  @Bean
  Queue supportPresenceQueue() {
    return new Queue(this.presenceQueue, true, true, true);
  }

  @Bean
  Queue supportTypingQueue() {
    return new Queue(this.typingQueue, true, true, true);
  }

  @Bean
  Binding bindingNewMessageQueue() {
    return BindingBuilder.bind(supportNewMessageQueue())
        .to(supportMessageExchange())
        .with("message");
  }

  @Bean
  Binding bindingPresenceQueue() {
    return BindingBuilder.bind(supportPresenceQueue())
        .to(supportMessageExchange())
        .with("presence");
  }

  @Bean
  Binding bindingTypingQueue() {
    return BindingBuilder.bind(supportTypingQueue()).to(supportMessageExchange()).with("typing");
  }

  @Bean
  Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    DefaultClassMapper classMapper = new DefaultClassMapper();
    classMapper.setTrustedPackages("com.ycyw.support.application.service.event.eventsdtos");
    converter.setClassMapper(classMapper);
    return converter;
  }
}
