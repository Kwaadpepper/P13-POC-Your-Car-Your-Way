package com.ycyw.support.application.route.chat;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.security.AuthenticatedUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationTypingController {
  private final Logger logger = LoggerFactory.getLogger(ConversationTypingController.class);

  private final SimpMessagingTemplate messaging;

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  public ConversationTypingController(SimpMessagingTemplate messaging) {
    this.messaging = messaging;
  }

  @MessageMapping("/typing")
  public void typing(
      TypingPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();

    // 1. Get user info
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug(
        "User {} with role {} is {} typing in conversation {}",
        userId,
        role,
        payload.isTyping() ? "" : "not",
        conversation);

    // 2. Broadcast TYPING event
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation,
        Map.of(
            "type",
            "typing",
            "payload",
            Map.of(
                "user",
                userId,
                "role",
                role,
                "conversation",
                conversation,
                "typing",
                payload.isTyping())));
  }

  public record TypingPayload(String conversation, boolean isTyping) {}
}
