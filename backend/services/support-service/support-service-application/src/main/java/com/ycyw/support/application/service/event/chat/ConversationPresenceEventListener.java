package com.ycyw.support.application.service.event.chat;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.UserPresence;
import com.ycyw.support.application.service.event.eventsdtos.PresenceEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConversationPresenceEventListener {
  private static final Logger logger =
      LoggerFactory.getLogger(ConversationPresenceEventListener.class);
  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final ChatRoomService chatRoomService;
  private final SimpMessagingTemplate messaging;

  public ConversationPresenceEventListener(
      ChatRoomService chatRoomService, SimpMessagingTemplate messaging) {
    this.chatRoomService = chatRoomService;
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
      logger.warn("Conversation {} not found, ignoring presence event", conversationId);
      return;
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
}
