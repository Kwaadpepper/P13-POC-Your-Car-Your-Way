package com.ycyw.support.application.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.model.valueobject.conversation.SenderType;
import com.ycyw.support.domain.usecase.conversation.SendMessage;

@Component
public class ConversationService {
  private final UseCaseExecutor useCaseExecutor;
  private final SendMessage.Handler useCaseHandler;

  public ConversationService(UseCaseExecutor useCaseExecutor, SendMessage.Handler useCaseHandler) {
    this.useCaseExecutor = useCaseExecutor;
    this.useCaseHandler = useCaseHandler;
  }

  public void sendMessage(UUID conversationId, String content, Sender sender) {
    final var input = new SendMessage.Message(conversationId, content, toMessageSender(sender));

    useCaseExecutor.execute(useCaseHandler, input);
  }

  private MessageSender toMessageSender(Sender sender) {
    return new MessageSender(sender.role(), sender.id());
  }

  public record Sender(UUID id, SenderType role) {}
}
