package com.ycyw.users.infrastructure.adapter.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

import org.jspecify.annotations.Nullable;

/**
 * In-memory implementation of the {@link UserRepository} interface. Stores users in a thread-safe
 * map for fast access and testing purposes.
 *
 * @see UserRepository
 */
public final class UserRepositoryInMemory implements UserRepository {
  private final Map<UUID, User> userStore = new ConcurrentHashMap<>();

  @Override
  public @Nullable User find(UUID id) {
    return userStore.get(id);
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
