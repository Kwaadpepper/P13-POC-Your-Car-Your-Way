package com.ycyw.support.application.route.chat;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationMessagesController {
  private final Logger logger = LoggerFactory.getLogger(ConversationMessagesController.class);

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final SimpMessagingTemplate messaging;
  private final ChatRoomService chatRoomService;

  public ConversationMessagesController(
      SimpMessagingTemplate messaging, ChatRoomService chatRoomService) {
    this.messaging = messaging;
    this.chatRoomService = chatRoomService;
  }

  // MESSAGE
  @MessageMapping("/send")
  public void send(
      SendPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug(
        "User {} with role {} sent message to conversation {}", userId, role, conversation);

    final var newMessage =
        chatRoomService.addMessage(conversation, userId, role, payload.text(), ZonedDateTime.now());

    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of("type", "message", "payload", toDto(newMessage)));
  }

  // HISTORY
  @MessageMapping("/history")
  public void history(HistoryPayload payload, SimpMessageHeaderAccessor headers) {

    final var conversation = payload.conversation();
    final var messages =
        chatRoomService.getAllMessages(conversation).stream().map(this::toDto).toList();

    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of(
            "type",
            "history",
            "payload",
            Map.of("conversation", conversation, "messages", messages)));
  }

  private MessageDto toDto(ChatMessage message) {
    return new MessageDto(
        message.id(),
        message.conversation(),
        new MessageDto.UserDto(message.user(), message.role()),
        message.text(),
        message.sentAt());
  }

  // PAYLOADS

  public record MessageEventPayload(
      UUID conversation, UserPayload from, String text, ZonedDateTime sentAt) {}

  public record UserPayload(UUID id, String role) {}

  public record SendPayload(UUID conversation, String text) {}

  public record HistoryPayload(UUID conversation, Integer limit) {}

  // DTO

  public static record MessageDto(
      UUID id, UUID conversation, UserDto from, String text, ZonedDateTime sentAt) {

    public static record UserDto(UUID id, String role) {}
  }
}
