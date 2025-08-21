package com.ycyw.support.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.support.domain.model.entity.faq.Faq;
import com.ycyw.support.domain.port.repository.FaqRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class FaqRepositoryInMemory implements FaqRepository {
  private static final Logger logger = LoggerFactory.getLogger(FaqRepositoryInMemory.class);
  private final Map<UUID, Faq> store = new ConcurrentHashMap<>();

  @Override
  public @Nullable Faq find(UUID id) {
    MDC.put("id", id.toString());
    try {
      @Nullable final Faq faq = store.get(id);
      logger.debug("Faq found with id {}: {}", id, faq);
      return faq;
    } finally {
      MDC.remove("id");
    }
  }

  @Override
  public void save(Faq entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved Faq with id {}", entity.getId());
  }

  @Override
  public void update(Faq entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated Faq with id {}", entity.getId());
  }

  @Override
  public void delete(Faq entity) {
    store.remove(entity.getId());
    logger.debug("Deleted Faq with id {}", entity.getId());
  }
}
