package com.ycyw.support.application.route.chat;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.config.RabbitMqChatConfig;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.event.eventsdtos.TypingEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationTypingController {
  private final Logger logger = LoggerFactory.getLogger(ConversationTypingController.class);

  private final RabbitTemplate rabbitTemplate;
  private final String brokerChatExchange;

  public ConversationTypingController(
      RabbitTemplate rabbitTemplate, RabbitMqChatConfig rabbitConfig) {
    this.rabbitTemplate = rabbitTemplate;
    this.brokerChatExchange = rabbitConfig.getExchange();
  }

  @MessageMapping("/typing")
  public void typing(
      TypingPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug(
        "User {} with role {} is {} typing in conversation {}",
        userId,
        role,
        payload.isTyping() ? "" : "not",
        conversation);

    rabbitTemplate.convertAndSend(
        brokerChatExchange,
        "typing",
        new TypingEvent(userId.toString(), role, conversation, payload.isTyping()));
  }

  public record TypingPayload(String conversation, boolean isTyping) {}
}
