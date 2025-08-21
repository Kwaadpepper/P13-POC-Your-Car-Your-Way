package com.ycyw.users.application.presenter;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.users.application.dto.OperatorViewDto;
import com.ycyw.users.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.users.domain.model.valueobject.Role;
import com.ycyw.users.domain.usecase.operator.FindOperator;

@Component
public class OperatorPresenter implements Presenter<OperatorViewDto, FindOperator.Output> {

  @Override
  public OperatorViewDto present(FindOperator.Output model) {
    if (model instanceof FindOperator.Output.FoundOperator foundOperator) {
      return toDto(foundOperator);
    }
    throw new ResourceNotFoundException("Client not found");
  }

  private OperatorViewDto toDto(FindOperator.Output.FoundOperator model) {
    return new OperatorViewDto(
        model.id(),
        model.name(),
        model.email().value(),
        toRoleStrings(model.roles()),
        model.updatedAt(),
        model.deletedAt());
  }

  private Set<String> toRoleStrings(Set<Role> roles) {
    return roles.stream().map(Role::name).collect(Collectors.toSet());
  }
}
