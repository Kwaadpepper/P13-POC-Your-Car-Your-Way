package com.ycyw.users.domain.port.repository;

import com.ycyw.shared.ddd.Repository;
import com.ycyw.users.domain.entity.User;

/**
 * Repository interface for managing {@link User} entities. Extends the generic {@link Repository}
 * interface for user-specific operations.
 *
 * @see User
 * @see Repository
 */
public interface UserRepository extends Repository<User> {}
