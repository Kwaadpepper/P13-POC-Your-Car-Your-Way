package com.ycyw.support.application.route.chat;

import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.UserPresence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationPresenceController {
  private final Logger logger = LoggerFactory.getLogger(ConversationPresenceController.class);

  private final SimpMessagingTemplate messaging;
  private final ChatRoomService chatRoomService;

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  public ConversationPresenceController(
      SimpMessagingTemplate messaging, ChatRoomService chatRoomService) {
    this.messaging = messaging;
    this.chatRoomService = chatRoomService;
  }

  @MessageMapping("/join")
  public void join(
      JoinPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug("User {} with role {} joined conversation {}", userId, role, conversation);

    final var presence = new UserPresence(userId, role, "online");
    chatRoomService.addParticipant(conversation, presence);

    // Broadcast JOIN event (list of participants)
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation,
        Map.of(
            "type",
            "join",
            "payload",
            Map.of(
                "conversation",
                conversation,
                "participants",
                chatRoomService.getParticipants(conversation))));

    // Broadcast PRESENCE event
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation,
        Map.of(
            "type",
            "presence",
            "payload",
            Map.of(
                "user", userId, "role", role, "status", "online", "conversation", conversation)));
  }

  @MessageMapping("/leave")
  public void leave(
      LeavePayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug("User {} with role {} left conversation {}", userId, role, conversation);

    chatRoomService.removeParticipant(conversation, userId);

    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation,
        Map.of(
            "type",
            "presence",
            "payload",
            Map.of(
                "user", userId, "role", role, "status", "offline", "conversation", conversation)));
  }

  public record JoinPayload(UUID conversation) {}

  public record LeavePayload(UUID conversation) {}
}
