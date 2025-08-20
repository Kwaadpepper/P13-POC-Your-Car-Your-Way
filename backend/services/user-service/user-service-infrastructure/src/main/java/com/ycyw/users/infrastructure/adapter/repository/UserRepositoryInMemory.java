package com.ycyw.users.infrastructure.adapter.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * In-memory implementation of the {@link UserRepository} interface. Stores users in a thread-safe
 * map for fast access and testing purposes.
 *
 * @see UserRepository
 */
@Repository
public class UserRepositoryInMemory implements UserRepository {
  private final Map<UUID, User> userStore = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(UserRepositoryInMemory.class);

  public UserRepositoryInMemory() {
    seedUserRepository();
  }

  @Override
  public @Nullable User find(UUID id) {
    MDC.put("id", id.toString());
    logger.debug("On tente de trouver l'utilisateur");
    @Nullable final User user = userStore.get(id);
    logger.debug("Utilisateur trouvé: {}", user);
    MDC.remove("id");
    return user;
  }

  @Override
  public void save(User entity) {
    userStore.put(entity.getId(), entity);
  }

  @Override
  public void update(User entity) {
    userStore.put(entity.getId(), entity);
  }

  @Override
  public void delete(User entity) {
    userStore.remove(entity.getId());
  }

  private void seedUserRepository() {
    this.save(new User("user@example.com"));
  }
}
