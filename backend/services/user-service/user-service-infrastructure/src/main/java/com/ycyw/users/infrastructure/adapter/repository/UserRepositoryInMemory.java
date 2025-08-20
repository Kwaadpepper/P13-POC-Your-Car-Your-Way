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

@Repository
public class UserRepositoryInMemory implements UserRepository {
  private final Map<UUID, User> userStore = new ConcurrentHashMap<>();
  private static final Logger logger = LoggerFactory.getLogger(UserRepositoryInMemory.class);

  @Override
  public @Nullable User findWithEmail(String email) {
    MDC.put("email", email);
    @Nullable final User user =
        userStore.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    logger.debug("User found with email {}: {}", email, user);
    MDC.remove("email");
    return user;
  }

  @Override
  public @Nullable User find(UUID id) {
    MDC.put("id", id.toString());
    @Nullable final User user = userStore.get(id);
    logger.debug("User found with id {}: {}", id, user);
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
}
