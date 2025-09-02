package com.ycyw.users.application.route.clients;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.exceptions.IllegalDomainStateException;
import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.users.application.dto.ApiErrorDetails;
import com.ycyw.users.application.dto.ClientViewDto;
import com.ycyw.users.application.exception.exceptions.BadRequestException;
import com.ycyw.users.application.presenter.ClientPresenter;
import com.ycyw.users.domain.usecase.client.FindClient;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FindClientController {
  private final UseCaseExecutor useCaseExecutor;
  private final FindClient.Handler getUserHandler;
  private final ClientPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(FindClientController.class);

  public FindClientController(
      UseCaseExecutor useCaseExecutor,
      FindClient.Handler getUserHandler,
      ClientPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.getUserHandler = getUserHandler;
    this.presenter = presenter;
  }

  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ClientViewDto.class))),
        @ApiResponse(
            responseCode = "400",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorDetails.class))),
        @ApiResponse(
            responseCode = "422",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorDetails.class)))
      })
  @GetMapping("/clients/{user}")
  public ClientViewDto getUserById(
      @PathVariable(name = "user", required = true) final String user) {

    @Nullable final UUID userId = toUserId(user);
    @Nullable final Email userEmail = toUserEmail(user);
    FindClient.@Nullable Input input = null;

    if (userId != null) {
      input = new FindClient.Input.FindById(userId);
    }
    if (userEmail != null) {
      input = new FindClient.Input.FindByEmail(userEmail);
    }

    if (input == null) {
      throw new BadRequestException("Invalid user identifier: " + user);
    }

    final var output = this.useCaseExecutor.execute(this.getUserHandler, input);

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

  private @Nullable Email toUserEmail(String email) {
    try {
      return new Email(email);
    } catch (IllegalDomainStateException e) {
      return null;
    }
  }
}
