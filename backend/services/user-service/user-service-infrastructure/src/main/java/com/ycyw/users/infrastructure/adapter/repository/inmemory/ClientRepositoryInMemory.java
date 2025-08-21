package com.ycyw.users.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.port.repository.ClientRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ClientRepositoryInMemory implements ClientRepository {
  private final Map<UUID, Client> store = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(ClientRepositoryInMemory.class);

  @Override
  public @Nullable Client find(UUID id) {
    MDC.put("id", id == null ? "null" : id.toString());
    @Nullable final Client client = store.get(id);
    logger.debug("Client found with id {}: {}", id, client);
    MDC.remove("id");
    return client;
  }

  @Override
  public @Nullable Client findByCredentialId(UUID credentialId) {
    MDC.put("credentialId", credentialId == null ? "null" : credentialId.toString());
    try {
      @Nullable final Client client =
          store.values().stream()
              .filter(o -> Objects.equals(o.getCredentialId().value(), credentialId))
              .findFirst()
              .orElse(null);
      logger.debug("Client found with credentialId {}: {}", credentialId, client);
      return client;
    } finally {
      MDC.remove("credentialId");
    }
  }

  @Override
  public void save(Client entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved client with id {}", entity.getId());
  }

  @Override
  public void update(Client entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated client with id {}", entity.getId());
  }

  @Override
  public void delete(Client entity) {
    store.remove(entity.getId());
    logger.debug("Deleted client with id {}", entity.getId());
  }

  @Override
  public @Nullable Client findByEmail(Email email) {
    MDC.put("email", email.value());
    @Nullable final Client client =
        store.values().stream()
            .filter(o -> Objects.equals(o.getEmail(), email))
            .findFirst()
            .orElse(null);
    logger.debug("Client found with email {}: {}", email, client);
    MDC.remove("email");
    return client;
  }
}
