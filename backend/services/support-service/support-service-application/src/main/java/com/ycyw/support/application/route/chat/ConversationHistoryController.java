package com.ycyw.support.application.route.chat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ycyw.support.application.service.chat.ChatRoomService;
import com.ycyw.support.application.service.chat.ChatRoomService.ChatMessage;
import com.ycyw.support.application.service.chat.ConversationService;
import com.ycyw.support.application.service.chat.ConversationService.ConversationMessage;

@Controller
@MessageMapping("/conversation")
public class ConversationHistoryController {
  private static final String CONVERSATION_TOPIC = "/topic/conversation/";

  private final SimpMessagingTemplate messaging;
  private final ConversationService conversationService;
  private final ChatRoomService chatRoomService;

  public ConversationHistoryController(
      SimpMessagingTemplate messaging,
      ConversationService conversationService,
      ChatRoomService chatRoomService) {
    this.messaging = messaging;
    this.conversationService = conversationService;
    this.chatRoomService = chatRoomService;
  }

  // HISTORY
  @MessageMapping("/history")
  public void history(HistoryPayload payload, SimpMessageHeaderAccessor headers) {
    final var conversation = payload.conversation();

    // 1. Fetch messages
    final var messages =
        chatRoomService.hasConversation(conversation)
            ? chatRoomService.getAllMessages(conversation).stream().map(this::toDto).toList()
            : fetchAllMessagesForConversation(conversation).stream().map(this::toDto).toList();

    // 2. Send messages to the user
    messaging.convertAndSend(
        CONVERSATION_TOPIC + conversation.toString(),
        Map.of(
            "type",
            "history",
            "payload",
            Map.of("conversation", conversation, "messages", messages)));
  }

  private List<ChatMessage> fetchAllMessagesForConversation(UUID conversationId) {
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

  private MessageDto toDto(ChatMessage message) {
    return new MessageDto(
        message.id(),
        message.conversation(),
        new MessageDto.UserDto(message.user(), message.role()),
        message.text(),
        message.sentAt());
  }

  public record HistoryPayload(UUID conversation) {}

  public static record MessageDto(
      UUID id, UUID conversation, UserDto from, String text, ZonedDateTime sentAt) {

    public static record UserDto(UUID id, String role) {}
  }
}
