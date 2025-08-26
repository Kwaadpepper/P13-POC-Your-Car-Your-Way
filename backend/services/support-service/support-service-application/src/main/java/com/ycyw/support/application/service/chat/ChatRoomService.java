package com.ycyw.support.application.service.chat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.ycyw.support.application.service.chat.ConversationService.ConversationMessage;
import com.ycyw.support.application.service.chat.ConversationService.UserRole;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Service that holds in-memory chat rooms data: messages and participants.
 *
 * <p>Thread-safe for use from WebSocket controller.
 *
 * <p>Note: payload/value types are defined as nested static records so they can be referenced from
 * controllers as ChatRoomService.MessageEventPayload, etc.
 */
@Component
public class ChatRoomService {
  private final ConversationService conversationService;

  private final Map<UUID, List<ChatMessage>> messagesByRoom = new ConcurrentHashMap<>();
  private final Map<UUID, Set<UserPresence>> roomParticipants = new ConcurrentHashMap<>();

  public ChatRoomService(ConversationService conversationService) {
    this.conversationService = conversationService;
  }

  // --- Messages operations ---

  /** Add a message to the conversation. */
  public ChatMessage addMessage(
      UUID conversation, UUID userId, String role, String text, ZonedDateTime sentAt) {

    final var newMessageId =
        conversationService.sendMessage(conversation, text, userId, mapToUserRole(role));

    final var chatMessage = new ChatMessage(newMessageId, conversation, userId, role, text, sentAt);

    // In-memory storage
    messagesByRoom
        .computeIfAbsent(conversation, k -> new CopyOnWriteArrayList<>())
        .add(chatMessage);

    return chatMessage;
  }

  /** Return all messages for a conversation (snapshot). */
  public List<ChatMessage> getAllMessages(UUID conversation) {
    final var conversationMessages =
        conversationService.getAllMessages(conversation).stream()
            .map(m -> mapToChatMessage(conversation, m))
            .toList();

    return Collections.unmodifiableList(
        new ArrayList<>(messagesByRoom.getOrDefault(conversation, conversationMessages)));
  }

  // --- Participants operations ---

  /** Add a participant (presence) to a conversation. */
  public void addParticipant(UUID conversation, UserPresence presence) {
    roomParticipants
        .computeIfAbsent(conversation, k -> ConcurrentHashMap.newKeySet())
        .add(presence);
  }

  /** Remove participant by user id. */
  public void removeParticipant(UUID conversation, UUID userId) {
    @Nullable final Set<UserPresence> set = roomParticipants.get(conversation);
    if (set == null) {
      return;
    }
    set.removeIf(p -> p.user().equals(userId));
  }

  /** Return participants snapshot for a conversation. */
  public Set<UserPresence> getParticipants(UUID conversation) {
    @Nullable final Set<UserPresence> set = roomParticipants.get(conversation);
    if (set == null) {
      return Set.of();
    }
    return Collections.unmodifiableSet(Set.copyOf(set));
  }

  // --- Data types ---

  public static record UserPresence(UUID user, String role, String status) {}

  public static record ChatMessage(
      UUID id, UUID conversation, UUID user, String role, String text, ZonedDateTime sentAt) {}

  private ChatMessage mapToChatMessage(UUID conversationId, ConversationMessage message) {
    return new ChatMessage(
        message.id(),
        conversationId,
        message.userId(),
        mapToRole(message.role()),
        message.text(),
        message.sentAt());
  }

  private UserRole mapToUserRole(String role) {
    return switch (role.toLowerCase()) {
      case "client" -> UserRole.CLIENT;
      case "operator" -> UserRole.OPERATOR;
      default -> throw new IllegalArgumentException("Unknown role: " + role);
    };
  }

  private String mapToRole(UserRole role) {
    return switch (role) {
      case CLIENT -> "client";
      case OPERATOR -> "operator";
    };
  }
}
