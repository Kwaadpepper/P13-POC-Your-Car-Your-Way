package com.ycyw.users.route;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.objectvalues.SimpleMessage;
import com.ycyw.users.dto.SimpleMessageDto;
import com.ycyw.users.presenter.SimpleMessagePresenter;

@RestController
public class RootController {
  private SimpleMessagePresenter presenter;

  public RootController(SimpleMessagePresenter presenter) {
    this.presenter = presenter;
  }

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessageDto index() {
    return this.presenter.present(new SimpleMessage("Welcome to the User Service!"));
  }
}
