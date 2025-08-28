package com.ycyw.support.application.route.chat;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.config.RabbitMqChatConfig;
import com.ycyw.support.application.security.AuthenticatedUser;
import com.ycyw.support.application.service.chat.ChatMessageEventSerializer;
import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@MessageMapping("/conversation")
public class ConversationMessagesController {
  private final Logger logger = LoggerFactory.getLogger(ConversationMessagesController.class);

  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final RabbitTemplate rabbitTemplate;
  private final ChatMessageEventSerializer chatMessageEventSerializer;
  private final SimpMessagingTemplate messaging;
  private final ChatRoomService chatRoomService;
  private final ConversationService conversationService;

  private final String brokerChatExchange;

  public ConversationMessagesController(
      RabbitMqChatConfig rabbitConfig,
      RabbitTemplate rabbitTemplate,
      ChatMessageEventSerializer chatMessageSerializer,
      SimpMessagingTemplate messaging,
      ChatRoomService chatRoomService,
      ConversationService conversationService) {
    this.rabbitTemplate = rabbitTemplate;
    this.chatMessageEventSerializer = chatMessageSerializer;
    this.messaging = messaging;
    this.chatRoomService = chatRoomService;
    this.conversationService = conversationService;
    this.brokerChatExchange = rabbitConfig.getExchange();
  }

  // MESSAGE
  @MessageMapping("/send")
  public void send(
      SendPayload payload, SimpMessageHeaderAccessor headers, Authentication authentication) {
    final var conversation = payload.conversation();
    final var text = payload.text();
    final var userDetails = (AuthenticatedUser) authentication.getPrincipal();
    final var userId = userDetails.getSubjectId();
    final var role = userDetails.getRole();

    logger.debug(
        "User {} with role {} sent message to conversation {}", userId, role, conversation);

    // 1. Persist message in conversation service
    final var newMessageId =
        conversationService.persistMessage(
            conversation, text, userId, UserRole.mapToUserRole(role));

    // 2. Remember in local chat room (in-memory)
    final var newMessage =
        chatRoomService.addMessage(
            newMessageId, conversation, userId, role, text, ZonedDateTime.now());

    // 3. Dispatch to RabbitMQ for other services
    final var payloadJson =
        chatMessageEventSerializer.serialize(mapToNewChatMessageEvent(newMessage));
    rabbitTemplate.convertAndSend(brokerChatExchange, "", payloadJson);

    // 4. Dispatch to WebSocket subscribers
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of("type", "message", "payload", toDto(newMessage)));
  }

  @RabbitListener(queues = "#{rabbitMqChatConfig.getNewMessageQueue()}")
  public void handleNewChatMessageEvent(String eventPayloadAsJson) {
    final var newChatMessageEvent = chatMessageEventSerializer.deserialize(eventPayloadAsJson);
    logger.debug("Received new chat message event from broker: {}", newChatMessageEvent);
    // Met à jour la mémoire sans repersister le message
    chatRoomService.addMessageFromRemote(mapToChatMessage(newChatMessageEvent));
  }

  private MessageDto toDto(ChatMessage message) {
    return new MessageDto(
        message.id(),
        message.conversation(),
        new MessageDto.UserDto(message.user(), message.role()),
        message.text(),
        message.sentAt());
  }

  private NewChatMessageEvent mapToNewChatMessageEvent(ChatMessage message) {
    return new NewChatMessageEvent(
        message.conversation(),
        message.id(),
        message.user(),
        message.role(),
        message.text(),
        message.sentAt());
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

  public record NewChatMessageEvent(
      UUID conversationId,
      UUID messageId,
      UUID senderId,
      String userRole,
      String content,
      ZonedDateTime timestamp)
      implements Serializable {}

  public record SendPayload(UUID conversation, String text) {}

  public static record MessageDto(
      UUID id, UUID conversation, UserDto from, String text, ZonedDateTime sentAt) {

    public static record UserDto(UUID id, String role) {}
  }
}
