package com.ycyw.users.application.route.clients;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.application.dto.ClientViewDto;
import com.ycyw.users.application.presenter.ClientPresenter;
import com.ycyw.users.domain.usecase.client.FindClient;

import jakarta.ws.rs.BadRequestException;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FindClientController {
  private final UseCaseExecutor useCaseExecutor;
  private final FindClient.FindUserHandler getUserHandler;
  private final ClientPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(FindClientController.class);

  public FindClientController(
      UseCaseExecutor useCaseExecutor,
      FindClient.FindUserHandler getUserHandler,
      ClientPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.getUserHandler = getUserHandler;
    this.presenter = presenter;
  }

  @GetMapping("/clients/{user}")
  public ClientViewDto getUserById(
      @PathVariable(name = "user", required = true) final String user) {

    final var userId = toUserId(user);
    final var userEmail = toUserEmail(user);
    final var output =
        this.useCaseExecutor.execute(
            this.getUserHandler,
            userId != null
                ? new FindClient.Input.FindClientById(userId)
                : new FindClient.Input.FindClientByEmail(userEmail));

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
