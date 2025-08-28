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

import org.eclipse.jdt.annotation.Nullable;

@Component
public class ChatRoomService {
  private final Map<UUID, List<ChatMessage>> messagesByRoom = new ConcurrentHashMap<>();
  private final Map<UUID, Set<UserPresence>> roomParticipants = new ConcurrentHashMap<>();

  public boolean hasConversation(UUID conversation) {
    return messagesByRoom.containsKey(conversation);
  }

  public void startConversation(UUID conversation, List<ChatMessage> initialMessages) {
    if (messagesByRoom.containsKey(conversation)) {
      throw new ConversationException("Conversation already started");
    }
    messagesByRoom.put(conversation, new CopyOnWriteArrayList<>(initialMessages));
    roomParticipants.put(conversation, ConcurrentHashMap.newKeySet());
  }

  public void endConversation(UUID conversation) {
    messagesByRoom.remove(conversation);
    roomParticipants.remove(conversation);
  }

  public ChatMessage addMessage(
      UUID newMessageId,
      UUID conversation,
      UUID userId,
      String role,
      String text,
      ZonedDateTime sentAt) {
    final var messages = conversationMessages(conversation);
    final var chatMessage = new ChatMessage(newMessageId, conversation, userId, role, text, sentAt);

    messages.add(chatMessage);

    return chatMessage;
  }

  public boolean addMessageFromRemote(ChatMessage message) {
    final var conversation = message.conversation();
    if (!hasConversation(conversation)) {
      // Conversation not started
      return false;
    }
    final var messages = conversationMessages(message.conversation());

    if (messages.stream().anyMatch(m -> m.id().equals(message.id()))) {
      // Message already present
      return false;
    }

    messages.add(message);

    return true;
  }

  public List<ChatMessage> getAllMessages(UUID conversation) {
    if (!hasConversation(conversation)) {
      throw new ConversationException("Conversation not started");
    }

    return Collections.unmodifiableList(
        new ArrayList<>(messagesByRoom.getOrDefault(conversation, List.of())));
  }

  public void addParticipant(UUID conversation, UserPresence presence) {
    final var participants = conversationParticipants(conversation);

    participants.add(presence);
    roomParticipants.put(conversation, participants);
  }

  public void removeParticipant(UUID conversation, UUID userId) {
    final var participants = conversationParticipants(conversation);

    participants.removeIf(p -> p.user().equals(userId));
  }

  public Set<UserPresence> getParticipants(UUID conversation) {
    final var participants = conversationParticipants(conversation);

    return Collections.unmodifiableSet(Set.copyOf(participants));
  }

  // Internal helpers

  private List<ChatMessage> conversationMessages(UUID conversation) {
    if (!hasConversation(conversation)) {
      throw new ConversationException("Conversation not started");
    }

    @Nullable final List<ChatMessage> messages = messagesByRoom.get(conversation);

    if (messages == null) {
      throw new ConversationException("Missing conversation " + conversation);
    }

    return messages;
  }

  private Set<UserPresence> conversationParticipants(UUID conversation) {
    if (!hasConversation(conversation)) {
      throw new ConversationException("Conversation not started");
    }

    @Nullable final Set<UserPresence> participants = roomParticipants.get(conversation);

    if (participants == null) {
      throw new ConversationException("Missing conversation " + conversation);
    }

    return participants;
  }

  public static record UserPresence(UUID user, String role, String status) {}

  public static record ChatMessage(
      UUID id, UUID conversation, UUID user, String role, String text, ZonedDateTime sentAt) {}

  public static class ConversationException extends RuntimeException {
    public ConversationException(String message) {
      super(message);
    }
  }
}
