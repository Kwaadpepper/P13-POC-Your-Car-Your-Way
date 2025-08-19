package com.ycyw.users.domain.usecase.user;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

/**
 * Handles the retrieval of a user by their unique identifier.
 *
 * <p>This use case delegates to the {@link UserRepository} to fetch user information.
 */
public class GetUserHandler implements UseCaseHandler<GetUser, User> {
  private final UserRepository userRepository;

  /**
   * Constructs a new {@code GetUserHandler} with the specified {@link UserRepository}.
   *
   * @param userRepository the repository used to retrieve user information
   */
  public GetUserHandler(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** Retrieves a user by their unique identifier. */
  public User execute(GetUser usecase) {
    final var userId = usecase.userId();

    return userRepository.find(userId);
  }
}
