package com.ycyw.users.domain.port.repository;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.users.domain.entity.User;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Repository interface for managing {@link User} entities. Extends the generic {@link Repository}
 * interface for user-specific operations.
 *
 * @see User
 * @see Repository
 */
public interface UserRepository extends Repository<User> {
  @Nullable User findWithEmail(String email);
}
