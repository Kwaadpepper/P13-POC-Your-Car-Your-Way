package com.ycyw.support.application.service.chat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
    startConversationIfNeeded(conversation);

    // Send message to conversation service (persist)
    final var newMessageId =
        conversationService.persistMessage(conversation, text, userId, mapToUserRole(role));

    // In-memory storage
    final var chatMessage = new ChatMessage(newMessageId, conversation, userId, role, text, sentAt);
    rememberMessage(conversation, chatMessage);

    return chatMessage;
  }

  /** Return all messages for a conversation (snapshot). */
  public List<ChatMessage> getAllMessages(UUID conversation) {
    startConversationIfNeeded(conversation);

    return Collections.unmodifiableList(
        new ArrayList<>(messagesByRoom.getOrDefault(conversation, List.of())));
  }

  // --- Participants operations ---

  /** Add a participant (presence) to a conversation. */
  public void addParticipant(UUID conversation, UserPresence presence) {
    @Nullable final Set<UserPresence> participants = roomParticipants.get(conversation);

    if (participants == null) {
      final var newParticipants = new HashSet<UserPresence>();
      newParticipants.add(presence);
      roomParticipants.put(conversation, newParticipants);
      return;
    }

    participants.add(presence);
    roomParticipants.put(conversation, participants);
  }

  /** Remove participant by user id. */
  public void removeParticipant(UUID conversation, UUID userId) {
    @Nullable final Set<UserPresence> participants = roomParticipants.get(conversation);
    if (participants == null) {
      return;
    }
    participants.removeIf(p -> p.user().equals(userId));
  }

  /** Return participants snapshot for a conversation. */
  public Set<UserPresence> getParticipants(UUID conversation) {
    @Nullable final Set<UserPresence> participants = roomParticipants.get(conversation);

    if (participants == null) {
      return Set.of();
    }

    return Collections.unmodifiableSet(Set.copyOf(participants));
  }

  private void startConversationIfNeeded(UUID conversationId) {
    if (!messagesByRoom.containsKey(conversationId)) {
      final var messages = fetchAllMesagesForConversation(conversationId);
      messagesByRoom.put(conversationId, new CopyOnWriteArrayList<>(messages));
    }
  }

  private void rememberMessage(UUID conversationId, ChatMessage message) {
    final var conversationMessages = getRememberedMessages(conversationId);
    conversationMessages.add(message);
    messagesByRoom.put(conversationId, conversationMessages);
  }

  private List<ChatMessage> getRememberedMessages(UUID conversationId) {
    return messagesByRoom.getOrDefault(conversationId, List.of());
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
