package com.ycyw.support.application.route;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.objectvalues.SimpleMessage;
import com.ycyw.support.application.dto.SimpleMessageDto;
import com.ycyw.support.application.presenter.SimpleMessagePresenter;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
public class RootController {
  private SimpleMessagePresenter presenter;

  public RootController(SimpleMessagePresenter presenter) {
    this.presenter = presenter;
  }

  @SecurityRequirements
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessageDto index() {
    return this.presenter.present(new SimpleMessage("Welcome to the Support Service!"));
  }
}
