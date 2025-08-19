package com.ycyw.users.infrastructure.adapter.repository;

import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the {@link UserRepository} interface. Stores users in a thread-safe
 * map for fast access and testing purposes.
 *
 * @see UserRepository
 */
public final class UserRepositoryInMemory {
  private final Map<UUID, User> userStore = new ConcurrentHashMap<>();

  /**
   * Finds a user by their unique identifier.
   *
   * @param id the UUID of the user to find
   * @return the User if found, otherwise null
   */
  public User find(UUID id) {
    return userStore.get(id);
  }

  /**
   * Saves a user to the in-memory store.
   *
   * @param entity the User to save
   */
  public void save(User entity) {
    userStore.put(entity.getId(), entity);
  }

  /**
   * Deletes a user from the in-memory store by their unique identifier.
   *
   * @param id the UUID of the user to delete
   */
  public void delete(UUID id) {
    userStore.remove(id);
  }
}
