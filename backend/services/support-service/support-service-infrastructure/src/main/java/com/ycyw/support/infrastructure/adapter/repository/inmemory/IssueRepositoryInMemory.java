package com.ycyw.support.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.port.repository.IssueRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class IssueRepositoryInMemory implements IssueRepository {
  private static final Logger logger = LoggerFactory.getLogger(IssueRepositoryInMemory.class);
  private final Map<UUID, Issue> store = new ConcurrentHashMap<>();

  @Override
  public @Nullable Issue find(UUID id) {
    MDC.put("id", id.toString());
    try {
      @Nullable final Issue issue = store.get(id);
      logger.debug("Issue found with id {}: {}", id, issue);
      return issue;
    } finally {
      MDC.remove("id");
    }
  }

  @Override
  public void save(Issue entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved Issue with id {}", entity.getId());
  }

  @Override
  public void update(Issue entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated Issue with id {}", entity.getId());
  }

  @Override
  public void delete(Issue entity) {
    store.remove(entity.getId());
    logger.debug("Deleted Issue with id {}", entity.getId());
  }
}
