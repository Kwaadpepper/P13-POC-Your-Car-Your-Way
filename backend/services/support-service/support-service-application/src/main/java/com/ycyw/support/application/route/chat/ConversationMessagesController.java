package com.ycyw.support.application.route.chat;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.config.RabbitMqChatConfig;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.UserRole;
import com.ycyw.support.application.service.event.eventsdtos.NewChatMessageEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationMessagesController {
  private final Logger logger = LoggerFactory.getLogger(ConversationMessagesController.class);

  private final RabbitTemplate rabbitTemplate;
  private final ChatRoomService chatRoomService;
  private final ConversationService conversationService;

  private final String brokerChatExchange;

  public ConversationMessagesController(
      RabbitMqChatConfig rabbitConfig,
      RabbitTemplate rabbitTemplate,
      ChatRoomService chatRoomService,
      ConversationService conversationService) {
    this.rabbitTemplate = rabbitTemplate;
    this.chatRoomService = chatRoomService;
    this.conversationService = conversationService;
    this.brokerChatExchange = rabbitConfig.getExchange();
  }

  // MESSAGE
  @MessageMapping("/send")
  public void send(
      SendPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var text = payload.text();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug(
        "User {} with role {} sent message to conversation {}", userId, role, conversation);

    // 1. Persist message in conversation service
    final var newMessageId =
        conversationService.persistMessage(
            conversation, text, userId, UserRole.mapToUserRole(role));

    // 2. Remember in local chat room (in-memory)
    final var newMessage =
        chatRoomService.addMessage(
            newMessageId, conversation, userId, role, text, ZonedDateTime.now());

    // 3. Dispatch to RabbitMQ for other services
    rabbitTemplate.convertAndSend(
        brokerChatExchange, "message", mapToNewChatMessageEvent(newMessage));
  }

  private NewChatMessageEvent mapToNewChatMessageEvent(ChatMessage message) {
    return new NewChatMessageEvent(
        message.conversation(),
        message.id(),
        message.user(),
        message.role(),
        message.text(),
        message.sentAt());
  }

  public record SendPayload(UUID conversation, String text) {}
}
