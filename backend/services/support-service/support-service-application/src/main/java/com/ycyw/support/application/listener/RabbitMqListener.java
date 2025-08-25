package com.ycyw.support.application.listener;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitMqListener {
  private final ObjectMapper objectMapper = new ObjectMapper();

  // private final ConversationWebSocketService webSocketService;

  // public RabbitMqListener(ConversationWebSocketService webSocketService) {
  //   this.webSocketService = webSocketService;
  // }

  @RabbitListener(queues = "chat-queue")
  public void receiveMessage(String eventJson) {
    try {
      ChatEvent event = objectMapper.readValue(eventJson, ChatEvent.class);
      // List<WebSocketSession> sessions =
      //     webSocketService.getSessionsForConversation(event.conversationId());
      // for (WebSocketSession session : sessions) {
      //   session.sendMessage(new TextMessage(eventJson));
      // }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public record ChatEvent(UUID conversationId, String sender, String content) {}
}
