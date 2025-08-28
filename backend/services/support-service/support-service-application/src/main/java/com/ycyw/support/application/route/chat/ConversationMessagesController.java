package com.ycyw.support.application.route.chat;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.config.RabbitMqChatConfig;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatMessageSerializer;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationMessagesController {
  private final Logger logger = LoggerFactory.getLogger(ConversationMessagesController.class);

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final ChatMessageSerializer chatMessageSerializer;
  private final SimpMessagingTemplate messaging;
  private final ChatRoomService chatRoomService;
  private final ConversationService conversationService;

  public ConversationMessagesController(
      SimpMessagingTemplate messaging, ChatRoomService chatRoomService) {
      SimpMessagingTemplate messaging,
      ChatRoomService chatRoomService,
      ConversationService conversationService) {
    this.rabbitConfig = rabbitConfig;
    this.rabbitTemplate = rabbitTemplate;
    this.chatMessageSerializer = chatMessageSerializer;
    this.messaging = messaging;
    this.chatRoomService = chatRoomService;
    this.conversationService = conversationService;
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

    // 4. Dispatch to WebSocket subscribers
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of("type", "message", "payload", toDto(newMessage)));
  }

  private MessageDto toDto(ChatMessage message) {
    return new MessageDto(
        message.id(),
        message.conversation(),
        new MessageDto.UserDto(message.user(), message.role()),
        message.text(),
        message.sentAt());
  }

  public record SendPayload(UUID conversation, String text) {}

  public static record MessageDto(
      UUID id, UUID conversation, UserDto from, String text, ZonedDateTime sentAt) {

    public static record UserDto(UUID id, String role) {}
  }
}
