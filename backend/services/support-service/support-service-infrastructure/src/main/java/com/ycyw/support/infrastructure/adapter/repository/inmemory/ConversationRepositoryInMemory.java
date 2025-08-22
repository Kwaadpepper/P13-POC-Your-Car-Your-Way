package com.ycyw.support.infrastructure.adapter.repository.inmemory;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.port.repository.ConversationRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ConversationRepositoryInMemory implements ConversationRepository {
  private static final Logger logger =
      LoggerFactory.getLogger(ConversationRepositoryInMemory.class);
  private final Map<UUID, Conversation> store = new ConcurrentHashMap<>();

  @Override
  public List<Conversation> findAll() {
    return store.values().stream().map(this::clone).toList();
  }

  @Override
  public @Nullable Conversation find(UUID id) {
    MDC.put("id", id.toString());
    try {
      @Nullable final Conversation conv = store.get(id);
      logger.debug("Conversation found with id {}: {}", id, conv);
      return conv != null ? clone(conv) : null;
    } finally {
      MDC.remove("id");
    }
  }

  @Override
  public void save(Conversation entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved Conversation with id {}", entity.getId());
  }

  @Override
  public void update(Conversation entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated Conversation with id {}", entity.getId());
  }

  @Override
  public void delete(Conversation entity) {
    store.remove(entity.getId());
    logger.debug("Deleted Conversation with id {}", entity.getId());
  }

  private Conversation clone(Conversation entity) {
    return Conversation.hydrate(entity.getId(), entity.getSubject(), entity.getIssue());
  }
}
