package com.ycyw.users.application.route.users;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.users.application.dto.UserViewDto;
import com.ycyw.users.application.presenter.UserPresenter;
import com.ycyw.users.domain.usecase.user.FindUser;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FindUserController {
  private final UseCaseExecutor useCaseExecutor;
  private final FindUser.FindUserHandler getUserHandler;
  private final UserPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(FindUserController.class);

  public FindUserController(
      UseCaseExecutor useCaseExecutor,
      FindUser.FindUserHandler getUserHandler,
      UserPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.getUserHandler = getUserHandler;
    this.presenter = presenter;
  }

  @GetMapping("/users/{user}")
  public UserViewDto getUserById(@PathVariable(name = "user", required = true) final String user) {

    final var userId = toUserId(user);
    final var userEmail = toUserEmail(user);
    final var output =
        this.useCaseExecutor.execute(
            this.getUserHandler,
            userId != null
                ? new FindUser.Input.FindUserById(userId)
                : new FindUser.Input.FindUserByEmail(userEmail));

    logger.info("User retrieved: {}", output);

    return this.presenter.present(output);
  }

  private @Nullable UUID toUserId(String user) {
    try {
      return UUID.fromString(user);
    } catch (IllegalArgumentException e) {
      logger.debug("Invalid user ID format: {}", user);
      return null;
    }
  }

  private String toUserEmail(String user) {
    return user;
  }
}
