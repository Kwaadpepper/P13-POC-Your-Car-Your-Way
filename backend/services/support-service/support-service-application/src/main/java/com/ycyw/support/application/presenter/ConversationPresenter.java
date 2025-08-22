package com.ycyw.support.application.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.support.application.dto.ConversationDto;
import com.ycyw.support.domain.usecase.conversation.GetAllConversation;

@Component
public class ConversationPresenter
    implements Presenter<List<ConversationDto>, GetAllConversation.Output> {

  @Override
  public List<ConversationDto> present(GetAllConversation.Output output) {
    if (output instanceof GetAllConversation.Output.All(var results)) {
      return results.stream().map(this::toDto).toList();
    }
    throw new IllegalArgumentException("Unexpected output type: " + output.getClass());
  }

  private ConversationDto toDto(GetAllConversation.Output.ConversationDto model) {
    return new ConversationDto(model.id(), model.subject(), model.issue());
  }
}
