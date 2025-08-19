package com.ycyw.users.application.presenter;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.users.application.dto.UserViewDto;
import com.ycyw.users.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.users.domain.entity.User;

import org.jspecify.annotations.Nullable;

@Component
public class UserPresenter implements Presenter<UserViewDto, @Nullable User> {

  @Override
  public UserViewDto present(@Nullable User model) {
    if (model == null) {
      throw new ResourceNotFoundException("User not found");
    }
    return toDto(model);
  }

  private UserViewDto toDto(User model) {
    return new UserViewDto(model.getId().toString(), model.getEmail());
  }
}
