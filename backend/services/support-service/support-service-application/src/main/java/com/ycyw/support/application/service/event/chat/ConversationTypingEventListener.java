package com.ycyw.support.application.service.event.chat;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ycyw.support.application.service.event.eventsdtos.TypingEvent;

@Component
public class ConversationTypingEventListener {
  private final SimpMessagingTemplate messaging;
  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  public ConversationTypingEventListener(SimpMessagingTemplate messaging) {
    this.messaging = messaging;
  }

  @RabbitListener(queues = "#{rabbitMqChatConfig.getTypingQueue()}")
  public void handleTypingEvent(TypingEvent event) {
    messaging.convertAndSend(
        CONVERSATION_TOPIC + event.conversation(),
        Map.of(
            "type",
            "typing",
            "payload",
            Map.of(
                "user", event.user(),
                "role", event.role(),
                "conversation", event.conversation(),
                "typing", event.typing())));
  }
}
