package com.ycyw.users.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.port.repository.OperatorRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class OperatorRepositoryInMemory implements OperatorRepository {
  private final Map<UUID, Operator> store = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(OperatorRepositoryInMemory.class);

  @Override
  public @Nullable Operator find(UUID id) {
    MDC.put("id", id == null ? "null" : id.toString());
    @Nullable final Operator operator = store.get(id);
    logger.debug("Operator found with id {}: {}", id, operator);
    MDC.remove("id");
    return operator;
  }

  @Override
  public @Nullable Operator findByCredentialId(UUID credentialId) {
    MDC.put("credentialId", credentialId == null ? "null" : credentialId.toString());
    @Nullable final Operator operator =
        store.values().stream()
            .filter(o -> Objects.equals(o.getCredentialId().value(), credentialId))
            .findFirst()
            .orElse(null);
    logger.debug("Operator found with credentialId {}: {}", credentialId, operator);
    MDC.remove("credentialId");
    return operator;
  }

  @Override
  public void save(Operator entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved operator with id {}", entity.getId());
  }

  @Override
  public void update(Operator entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated operator with id {}", entity.getId());
  }

  @Override
  public void delete(Operator entity) {
    store.remove(entity.getId());
    logger.debug("Deleted operator with id {}", entity.getId());
  }

  @Override
  public @Nullable Operator findByEmail(Email email) {
    MDC.put("email", email.value());
    @Nullable final Operator operator =
        store.values().stream()
            .filter(o -> Objects.equals(o.getEmail(), email))
            .findFirst()
            .orElse(null);
    logger.debug("Operator found with email {}: {}", email, operator);
    MDC.remove("email");
    return operator;
  }
}
