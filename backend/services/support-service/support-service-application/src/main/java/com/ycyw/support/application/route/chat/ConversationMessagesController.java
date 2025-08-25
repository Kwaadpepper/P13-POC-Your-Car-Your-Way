package com.ycyw.support.application.route.chat;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.MessageEventPayload;
import com.ycyw.support.application.service.chat.ChatRoomService.User;

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

    MessageEventPayload msg =
        new MessageEventPayload(
            UuidV7.randomUuid(),
            conversation,
            new User(userId, "toto", role),
            payload.text(),
            ZonedDateTime.now());

    chatRoomService.addMessage(conversation, msg);

    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(), Map.of("type", "message", "payload", msg));
  }

  // HISTORY
  @MessageMapping("/history")
  @SendToUser("/queue/history")
  public Map<String, Object> history(HistoryPayload payload, SimpMessageHeaderAccessor headers) {

    final var conversation = payload.conversation();
    final var slice =
        chatRoomService.getLastMessages(
            conversation, payload.limit() != null ? payload.limit() : 50);

    return Map.of(
        "type", "history", "payload", Map.of("conversation", conversation, "messages", slice));
  }

  // Payload DTOs (kept as controller-local because they are request payloads)

  public record SendPayload(UUID conversation, String text) {}

  public record HistoryPayload(UUID conversation, Integer limit) {}
}
