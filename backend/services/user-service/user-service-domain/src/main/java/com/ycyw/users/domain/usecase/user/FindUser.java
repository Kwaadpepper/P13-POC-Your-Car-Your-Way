package com.ycyw.users.domain.usecase.user;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.users.domain.entity.User;
import com.ycyw.users.domain.port.repository.UserRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface FindUser {
  sealed interface Input extends UseCaseInput, FindUser {
    record FindUserById(UUID userId) implements Input {}

    record FindUserByEmail(String email) implements Input {}
  }

  record FoundUser(UUID id, String email) implements UseCaseOutput, FindUser {}

  final class FindUserHandler implements UseCaseHandler<Input, @Nullable FoundUser>, FindUser {
    private final UserRepository userRepository;

    public FindUserHandler(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    public @Nullable FoundUser handle(Input usecaseInput) {
      return switch (usecaseInput) {
        case Input.FindUserById getUserById -> run(getUserById);
        case Input.FindUserByEmail getUserByEmail -> run(getUserByEmail);
      };
    }

    private @Nullable FoundUser run(Input.FindUserById usecaseInput) {
      final var id = usecaseInput.userId();

      final var user = userRepository.find(id);
      return mapToFoundUser(user);
    }

    private @Nullable FoundUser run(Input.FindUserByEmail usecaseInput) {
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
