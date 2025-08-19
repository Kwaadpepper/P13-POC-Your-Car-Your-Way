package com.ycyw.users.domain.usecase.user;

import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;
import java.util.UUID;

/**
 * Handles the retrieval of a user by their unique identifier.
 *
 * <p>This use case delegates to the {@link UserRepository} to fetch user information.
 */
public class GetUserHandler {
  private final UserRepository userRepository;

  /**
   * Constructs a new {@code GetUserHandler} with the specified {@link UserRepository}.
   *
   * @param userRepository the repository used to retrieve user information
   */
  public GetUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param userId the unique identifier of the user to retrieve
   * @return the {@link User} corresponding to the given identifier, or {@code null} if not found
   */
  public User execute(UUID userId) {
    return userRepository.find(userId);
  }
}
