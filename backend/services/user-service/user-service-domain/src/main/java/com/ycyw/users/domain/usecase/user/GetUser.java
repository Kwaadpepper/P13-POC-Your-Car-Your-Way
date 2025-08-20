package com.ycyw.users.domain.usecase.user;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface GetUser {
  sealed interface Input extends UseCaseInput, GetUser {
    record GetUserById(UUID userId) implements Input {}

    record GetUserByEmail(String email) implements Input {}
  }

  record FoundUser(UUID id, String email) implements UseCaseOutput, GetUser {}

  final class GetUserHandler implements UseCaseHandler<Input, @Nullable FoundUser>, GetUser {
    private final UserRepository userRepository;

    public GetUserHandler(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    public @Nullable FoundUser execute(Input usecaseInput) {
      return switch (usecaseInput) {
        case Input.GetUserById getUserById -> run(getUserById);
        case Input.GetUserByEmail getUserByEmail -> run(getUserByEmail);
      };
    }

    private @Nullable FoundUser run(Input.GetUserById usecaseInput) {
      final var id = usecaseInput.userId();

      final var user = userRepository.find(id);
      return mapToFoundUser(user);
    }

    private @Nullable FoundUser run(Input.GetUserByEmail usecaseInput) {
      final var email = usecaseInput.email();

      final var user = userRepository.findWithEmail(email);
      return mapToFoundUser(user);
    }

    private @Nullable FoundUser mapToFoundUser(@Nullable User user) {
      if (user == null) {
        return null;
      }
      return new FoundUser(user.getId(), user.getEmail());
    }
  }
}
