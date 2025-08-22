package com.ycyw.users.application.route.operators;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.application.dto.OperatorViewDto;
import com.ycyw.users.application.presenter.OperatorPresenter;
import com.ycyw.users.domain.usecase.operator.FindOperator;

import jakarta.ws.rs.BadRequestException;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FindOperatorController {
  private final UseCaseExecutor useCaseExecutor;
  private final FindOperator.Handler getUserHandler;
  private final OperatorPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(FindOperatorController.class);

  public FindOperatorController(
      UseCaseExecutor useCaseExecutor,
      FindOperator.Handler getUserHandler,
      OperatorPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.getUserHandler = getUserHandler;
    this.presenter = presenter;
  }

  @GetMapping("/operators/{user}")
  public OperatorViewDto getUserById(
      @PathVariable(name = "user", required = true) final String user) {

    final var userId = toUserId(user);
    final var userEmail = toUserEmail(user);
    final var output =
        this.useCaseExecutor.execute(
            this.getUserHandler,
            userId != null
                ? new FindOperator.Input.FindById(userId)
                : new FindOperator.Input.FindByEmail(userEmail));

    logger.info("User retrieved: {}", output);

    return this.presenter.present(output);
  }

  private @Nullable UUID toUserId(String id) {
    try {
      return UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      logger.debug("Invalid user ID format: {}", id);
      return null;
    }
  }

  private Email toUserEmail(String email) {
    try {
      return new Email(email);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid email format: " + email);
    }
  }
}
