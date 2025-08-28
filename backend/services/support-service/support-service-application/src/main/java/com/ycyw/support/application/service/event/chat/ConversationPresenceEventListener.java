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
    logger.debug(
        "Received presence event for conversation {}: user {} is {}",
        event.conversationId(),
        event.userId(),
        event.status());

    if (event.status().equals(PresenceEvent.Status.ONLINE)) {
      chatRoomService.addParticipant(
          event.conversationId(),
          new UserPresence(event.userId(), event.role(), event.status().toString()));
    }
    if (event.status().equals(PresenceEvent.Status.ONLINE)) {
      chatRoomService.removeParticipant(event.conversationId(), event.userId());
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
