package com.ycyw.users.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.valueobject.HashedIdentifier;
import com.ycyw.users.domain.port.repository.CredentialRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class CredentialRepositoryInMemory implements CredentialRepository {
  private final Map<UUID, Credential> store = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(CredentialRepositoryInMemory.class);

  @Override
  public @Nullable Credential find(UUID id) {
    MDC.put("id", id == null ? "null" : id.toString());
    @Nullable final Credential credential = store.get(id);
    logger.debug("Credential found with id {}: {}", id, credential);
    MDC.remove("id");
    return credential;
  }

  @Override
  public @Nullable Credential findByIdentifier(HashedIdentifier identifier) {
    @Nullable final Credential credential =
        store.values().stream()
            .filter(
                c -> {
                  var id = c.getHashedIdentifier();
                  return id != null && id.equals(identifier);
                })
            .findFirst()
            .orElse(null);
    return credential;
  }

  @Override
  public void save(Credential entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved credential with id {}", entity.getId());
  }

  @Override
  public void update(Credential entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated credential with id {}", entity.getId());
  }

  @Override
  public void delete(Credential entity) {
    store.remove(entity.getId());
    logger.debug("Deleted credential with id {}", entity.getId());
  }
}
