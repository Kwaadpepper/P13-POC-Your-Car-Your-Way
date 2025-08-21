package com.ycyw.users.application.presenter;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.shared.ddd.objectvalues.SimpleMessage;
import com.ycyw.users.application.dto.SimpleMessageDto;

@Component
public class SimpleMessagePresenter implements Presenter<SimpleMessageDto, SimpleMessage> {

  @Override
  public SimpleMessageDto present(SimpleMessage model) {
    return new SimpleMessageDto(model.value());
  }
}
