package com.ycyw.users.domain.usecase.user;

import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

public sealed interface CreateUser {
  record CreateUserInput(String email) implements UseCaseInput, CreateUser {}

  record CreatedUser(String email) implements UseCaseOutput, CreateUser {}

  final class CreateUserHandler
      implements UseCaseHandler<CreateUserInput, CreatedUser>, CreateUser {
    private final UserRepository userRepository;

    public CreateUserHandler(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    public CreatedUser handle(CreateUserInput usecaseInput) {
      final var email = usecaseInput.email();

      if (userRepository.findWithEmail(email) != null) {
        throw new IllegalDomainStateException(
            "User with email %s already exists.".formatted(email));
      }

      final var user = new User(email);
      userRepository.save(user);
      return mapToCreatedUser(user);
    }

    private CreatedUser mapToCreatedUser(User user) {
      return new CreatedUser(user.getEmail());
    }
  }
}
