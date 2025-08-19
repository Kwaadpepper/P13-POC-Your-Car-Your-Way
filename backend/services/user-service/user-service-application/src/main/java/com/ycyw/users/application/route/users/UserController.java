package com.ycyw.users.application.route.users;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.users.application.dto.UserViewDto;
import com.ycyw.users.application.presenter.UserPresenter;
import com.ycyw.users.domain.usecase.user.GetUser;
import com.ycyw.users.domain.usecase.user.GetUserHandler;

@RestController
public class UserController {
  private final UseCaseExecutor useCaseExecutor;
  private final GetUserHandler getUserHandler;
  private final UserPresenter presenter;

  public UserController(
      UseCaseExecutor useCaseExecutor, GetUserHandler getUserHandler, UserPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.getUserHandler = getUserHandler;
    this.presenter = presenter;
  }

  @GetMapping("/users/{userId}")
  public UserViewDto hello(@PathVariable(name = "userId", required = true) final UUID userId) {

    final var getUser = new GetUser(userId);
    final var output = this.useCaseExecutor.execute(this.getUserHandler, getUser);

    return this.presenter.present(output);
  }
}
