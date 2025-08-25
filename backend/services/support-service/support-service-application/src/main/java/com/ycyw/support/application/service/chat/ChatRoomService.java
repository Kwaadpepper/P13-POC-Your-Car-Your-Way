package com.ycyw.support.application.service.chat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

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

  private final Map<UUID, List<MessageEventPayload>> messagesByRoom = new ConcurrentHashMap<>();
  private final Map<UUID, Set<UserPresence>> roomParticipants = new ConcurrentHashMap<>();

  // --- Models used by controller / service consumers ---

  public static record MessageEventPayload(
      UUID id, UUID conversation, User from, String text, ZonedDateTime sentAt) {}

  public static record User(UUID id, String name, String role) {}

  public static record UserPresence(UUID user, String role, String status) {}

  // --- Messages operations ---

  /** Add a message to the conversation. */
  public void addMessage(UUID conversation, MessageEventPayload msg) {
    messagesByRoom
        .computeIfAbsent(conversation, k -> new java.util.concurrent.CopyOnWriteArrayList<>())
        .add(msg);
  }

  /** Return all messages for a conversation (snapshot). */
  public List<MessageEventPayload> getAllMessages(String conversation) {
    @Nullable final List<MessageEventPayload> list = messagesByRoom.get(conversation);
    if (list == null) {
      return List.of();
    }
    return Collections.unmodifiableList(new ArrayList<>(list));
  }

  /** Return the last 'limit' messages for a conversation. */
  public List<MessageEventPayload> getLastMessages(UUID conversation, int limit) {
    @Nullable final List<MessageEventPayload> all = messagesByRoom.getOrDefault(conversation, List.of());
    if (all.isEmpty()) {
      return List.of();
    }
    final int size = all.size();
    final int from = Math.max(0, size - limit);
    return Collections.unmodifiableList(new ArrayList<>(all.subList(from, size)));
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
}
