package com.ycyw.users.application.presenter;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.users.application.dto.UserViewDto;
import com.ycyw.users.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.users.domain.usecase.user.GetUser;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class UserPresenter implements Presenter<UserViewDto, GetUser.@Nullable FoundUser> {

  @Override
  public UserViewDto present(GetUser.@Nullable FoundUser model) {
    if (model == null) {
      throw new ResourceNotFoundException("User not found");
    }
    return toDto(model);
  }

  private UserViewDto toDto(GetUser.FoundUser model) {
    return new UserViewDto(model.id().toString(), model.email());
  }
}
