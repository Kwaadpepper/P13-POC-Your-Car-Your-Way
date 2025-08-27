package com.ycyw.support.application.route.chat;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.objectvalues.SimpleMessage;
import com.ycyw.support.application.dto.SimpleMessageDto;
import com.ycyw.support.application.presenter.SimpleMessagePresenter;

@RestController
public class PingController {
  private SimpleMessagePresenter presenter;

  public PingController(SimpleMessagePresenter presenter) {
    this.presenter = presenter;
  }

  @GetMapping(value = "/chat/ping", produces = MediaType.APPLICATION_JSON_VALUE)
  public SimpleMessageDto index() {
    return this.presenter.present(new SimpleMessage("Pong"));
  }
}
