package com.ycyw.support.application.route.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ChatRoomService.UserPresence;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.ConversationMessage;
import com.ycyw.support.application.service.event.eventsdtos.PresenceEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationPresenceController {
  private final Logger logger = LoggerFactory.getLogger(ConversationPresenceController.class);

  private final SimpMessagingTemplate messaging;
  private final ChatRoomService chatRoomService;
  private final ConversationService conversationService;

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final RabbitTemplate rabbitTemplate;
  private final String brokerChatExchange;

  public ConversationPresenceController(
      SimpMessagingTemplate messaging,
      ChatRoomService chatRoomService,
      ConversationService conversationService,
      RabbitTemplate rabbitTemplate,
      RabbitMqChatConfig rabbitConfig) {
    this.messaging = messaging;
    this.chatRoomService = chatRoomService;
    this.conversationService = conversationService;
    this.rabbitTemplate = rabbitTemplate;
    this.brokerChatExchange = rabbitConfig.getExchange();
  }

  @MessageMapping("/join")
  public void join(
      JoinPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug("User {} with role {} joined conversation {}", userId, role, conversation);

    // 1. Start the conversation if not already started
    if (!chatRoomService.hasConversation(conversation)) {
      final var initialMessages = fetchAllMesagesForConversation(conversation);
      chatRoomService.startConversation(conversation, initialMessages);
    }

    // 2. Add participant
    final var presence = new UserPresence(userId, role, "online");
    chatRoomService.addParticipant(conversation, presence);

    // 3. Notify presence via RabbitMQ (to handle multiple instances)
    rabbitTemplate.convertAndSend(
        brokerChatExchange,
        "presence",
        new PresenceEvent(conversation, userId, role, PresenceEvent.Status.ONLINE));

    // 4. Send current participants to the new user
    messaging.convertAndSendToUser(
        userId.toString(),
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
  }

  @MessageMapping("/leave")
  public void leave(
      LeavePayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();

    // 1. Get user info
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug("User {} with role {} left conversation {}", userId, role, conversation);

    // 2. Remove participant
    chatRoomService.removeParticipant(conversation, userId);

    // 3. If no more participants, end the conversation
    if (chatRoomService.getParticipants(conversation).isEmpty()) {
      logger.debug("No more participants in conversation {}, cleaning up", conversation);
      chatRoomService.endConversation(conversation);
      return;
    }

    // 4. Notify presence via RabbitMQ (to handle multiple instances)
    rabbitTemplate.convertAndSend(
        brokerChatExchange,
        "presence",
        new PresenceEvent(conversation, userId, role, PresenceEvent.Status.OFFLINE));
  }

  private List<ChatMessage> fetchAllMesagesForConversation(UUID conversationId) {
    final var conversationMessages = conversationService.getAllMessages(conversationId);

    if (conversationMessages == null) {
      return List.of();
    }

    return Collections.unmodifiableList(
        new ArrayList<>(
            conversationMessages.stream().map(m -> mapToChatMessage(conversationId, m)).toList()));
  }

  private ChatMessage mapToChatMessage(UUID conversationId, ConversationMessage message) {
    return new ChatMessage(
        message.id(),
        conversationId,
        message.userId(),
        message.role().toString(),
        message.text(),
        message.sentAt());
  }

  public record JoinPayload(UUID conversation) {}

  public record LeavePayload(UUID conversation) {}
}
