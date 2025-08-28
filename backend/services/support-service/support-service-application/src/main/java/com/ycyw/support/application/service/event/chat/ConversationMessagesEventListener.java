package com.ycyw.support.application.service.event.chat;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ycyw.support.application.dto.chat.MessageDto;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.event.eventsdtos.NewChatMessageEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConversationMessagesEventListener {
  private static final Logger logger =
      LoggerFactory.getLogger(ConversationMessagesEventListener.class);
  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final ChatRoomService chatRoomService;
  private final SimpMessagingTemplate messaging;

  public ConversationMessagesEventListener(
      ChatRoomService chatRoomService, SimpMessagingTemplate messaging) {
    this.chatRoomService = chatRoomService;
    this.messaging = messaging;
  }

  @RabbitListener(queues = "#{rabbitMqChatConfig.getNewMessageQueue()}")
  public void handleNewChatMessageEvent(NewChatMessageEvent newChatMessageEvent) {
    final var conversation = newChatMessageEvent.conversationId();
    final var chatMessage = mapToChatMessage(newChatMessageEvent);

    logger.debug(
        "Received new chat message event for conversation {} from user {}",
        chatMessage.conversation(),
        chatMessage.user());

    // 1. Store message in local chat room service
    chatRoomService.addMessageFromRemote(chatMessage);

    // 2. Dispatch message to WebSocket subscribers
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of("type", "message", "payload", toDto(chatMessage)));
  }

  private ChatMessage mapToChatMessage(NewChatMessageEvent event) {
    return new ChatMessage(
        event.messageId(),
        event.conversationId(),
        event.senderId(),
        event.userRole(),
        event.content(),
        event.timestamp());
  }

  private MessageDto toDto(ChatMessage message) {
    return new MessageDto(
        message.id(),
        message.conversation(),
        new MessageDto.UserDto(message.user(), message.role()),
        message.text(),
        message.sentAt());
  }
}
