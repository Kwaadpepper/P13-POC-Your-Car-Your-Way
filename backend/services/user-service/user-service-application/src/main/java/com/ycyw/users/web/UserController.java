package com.ycyw.users.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.objectvalues.SimpleMessage;
import com.ycyw.users.dto.SimpleMessageDto;
import com.ycyw.users.presenter.SimpleMessagePresenter;

import org.jspecify.annotations.Nullable;

@RestController
public class UserController {
  private final SimpleMessagePresenter presenter;

  public UserController(SimpleMessagePresenter presenter) {
    this.presenter = presenter;
  }

  @GetMapping("/users/hello")
  public SimpleMessageDto hello(
      @RequestParam(name = "name", required = false) @Nullable String name) {
    var who = (name == null || name.isBlank()) ? "user-service" : name;
    return this.presenter.present(new SimpleMessage("Hello, " + who + "!"));
  }
}
