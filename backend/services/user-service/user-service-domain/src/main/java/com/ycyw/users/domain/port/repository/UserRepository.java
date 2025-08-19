package com.ycyw.users.domain.port.repository;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.users.domain.entity.User;

import org.jspecify.annotations.Nullable;

/**
 * Repository interface for managing {@link User} entities. Extends the generic {@link Repository}
 * interface for user-specific operations.
 *
 * @see User
 * @see Repository
 */
public interface UserRepository extends Repository<User> {
  @Nullable User find(UUID id);
}
