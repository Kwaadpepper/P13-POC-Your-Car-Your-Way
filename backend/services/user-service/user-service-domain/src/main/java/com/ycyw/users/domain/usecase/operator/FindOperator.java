package com.ycyw.users.domain.usecase.operator;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.domain.model.entity.operator.Operator;
import com.ycyw.users.domain.model.valueobject.Role;
import com.ycyw.users.domain.port.repository.OperatorRepository;

import org.eclipse.jdt.annotation.Nullable;

public sealed interface FindOperator {
  sealed interface Input extends UseCaseInput, FindOperator {
    record FindById(UUID userId) implements Input {}

    record FindByEmail(Email email) implements Input {}
  }

  sealed interface Output extends UseCaseOutput, FindOperator {
    record FoundOperator(
        UUID id,
        String name,
        Email email,
        Set<Role> roles,
        ZonedDateTime updatedAt,
        @Nullable ZonedDateTime deletedAt)
        implements Output {}

    record NotFound() implements Output {}
  }

  final class Handler implements UseCaseHandler<Input, Output>, FindOperator {
    private final OperatorRepository operatorRepository;

    public Handler(OperatorRepository operatorRepository) {
      this.operatorRepository = operatorRepository;
    }

    @Override
    public Output handle(Input usecaseInput) {
      return switch (usecaseInput) {
        case Input.FindById getUserById -> run(getUserById);
        case Input.FindByEmail getUserByEmail -> run(getUserByEmail);
      };
    }

    private Output run(Input.FindById usecaseInput) {
      final var id = usecaseInput.userId();

      final var operator = operatorRepository.find(id);

      if (operator == null) {
        return new Output.NotFound();
      }

      return mapToFoundOperator(operator);
    }

    private Output run(Input.FindByEmail usecaseInput) {
      final var email = usecaseInput.email();

      final var operator = operatorRepository.findByEmail(email);

      if (operator == null) {
        return new Output.NotFound();
      }

      return mapToFoundOperator(operator);
    }

    private Output.FoundOperator mapToFoundOperator(Operator operator) {
      return new Output.FoundOperator(
          operator.getId(),
          operator.getName(),
          operator.getEmail(),
          operator.getRoles(),
          operator.getUpdatedAt(),
          operator.getDeletedAt());
    }
  }
}
