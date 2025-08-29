package com.ycyw.support.application.service.event.chat;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ChatRoomService.UserPresence;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.ConversationMessage;
import com.ycyw.support.application.service.event.eventsdtos.PresenceEvent;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConversationPresenceEventListener {
  private static final Logger logger =
      LoggerFactory.getLogger(ConversationPresenceEventListener.class);
  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final ChatRoomService chatRoomService;
  private final ConversationService conversationService;
  private final SimpMessagingTemplate messaging;

  public ConversationPresenceEventListener(
      ChatRoomService chatRoomService,
      ConversationService conversationService,
      SimpMessagingTemplate messaging) {
    this.chatRoomService = chatRoomService;
    this.conversationService = conversationService;
    this.messaging = messaging;
  }

  @RabbitListener(queues = "#{rabbitMqChatConfig.getPresenceQueue()}")
  public void handlePresenceEvent(PresenceEvent event) {
    final var conversationId = event.conversationId();
    logger.debug(
        "Received presence event for conversation {}: user {} is {}",
        event.conversationId(),
        event.userId(),
        event.status());

    if (!chatRoomService.hasConversation(conversationId)) {
      final @Nullable List<ConversationMessage> conversationMessages =
          conversationService.getAllMessages(conversationId);
      final var chatMessages =
          (conversationMessages != null ? conversationMessages : List.<ConversationMessage>of())
              .stream().map(message -> mapToChatMessage(conversationId, message)).toList();
      chatRoomService.startConversation(conversationId, chatMessages);
    }

    switch (event.status()) {
      case ONLINE ->
          chatRoomService.addParticipant(
              event.conversationId(),
              new UserPresence(event.userId(), event.role(), event.status().value()));
      case OFFLINE -> chatRoomService.removeParticipant(event.conversationId(), event.userId());
      default -> logger.warn("Unknown presence status: {}", event.status());
    }

    // Resdistribute presence event to WebSocket subscribers
    messaging.convertAndSend(
        CONVERSATION_TOPIC + event.conversationId(),
        Map.of(
            "type",
            "presence",
            "payload",
            Map.of(
                "user", event.userId(),
                "role", event.role(),
                "status", event.status().value(),
                "conversation", event.conversationId())));
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
}
