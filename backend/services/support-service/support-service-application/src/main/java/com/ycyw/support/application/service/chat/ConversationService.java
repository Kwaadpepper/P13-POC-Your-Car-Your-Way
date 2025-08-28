package com.ycyw.support.application.service.chat;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.domain.model.valueobject.conversation.MessageSender;
import com.ycyw.support.domain.model.valueobject.conversation.SenderType;
import com.ycyw.support.domain.usecase.conversation.CreateMessage;
import com.ycyw.support.domain.usecase.conversation.GetConversationMessages;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class ConversationService {
  private final UseCaseExecutor useCaseExecutor;
  private final GetConversationMessages.Handler getHistoryHandler;
  private final CreateMessage.Handler sendMessageHandler;

  public ConversationService(
      UseCaseExecutor useCaseExecutor,
      GetConversationMessages.Handler getHistoryHandler,
      CreateMessage.Handler sendMessageHandler) {
    this.useCaseExecutor = useCaseExecutor;
    this.getHistoryHandler = getHistoryHandler;
    this.sendMessageHandler = sendMessageHandler;
  }

  public UUID persistMessage(UUID conversationId, String content, UUID senderId, UserRole role) {
    final var input =
        new CreateMessage.Message(conversationId, content, toMessageSender(senderId, role));

    final var output = useCaseExecutor.execute(sendMessageHandler, input);

    return output.messageId();
  }

  public @Nullable List<ConversationMessage> getAllMessages(UUID conversationId) {
    final var input = new GetConversationMessages.ForConversation(conversationId);
    final var output = useCaseExecutor.execute(getHistoryHandler, input);

    return switch (output) {
      case GetConversationMessages.Output.Messages(var value) ->
          value.stream().map(this::mapToChatMessage).toList();
      case GetConversationMessages.Output.NoSuchConversation() -> null;
    };
  }

  private ConversationMessage mapToChatMessage(
      GetConversationMessages.Output.Messages.MessageDto message) {
    return new ConversationMessage(
        message.id(),
        message.sender().id(),
        UserRole.mapToUserRole(message.sender().type()),
        message.content(),
        message.sentAt());
  }

  public static record ConversationMessage(
      UUID id, UUID userId, UserRole role, String text, ZonedDateTime sentAt) {}

  public enum UserRole {
    CLIENT,
    OPERATOR;

    public static UserRole mapToUserRole(String role) {
      return switch (role.toLowerCase()) {
        case "client" -> UserRole.CLIENT;
        case "operator" -> UserRole.OPERATOR;
        default -> throw new IllegalArgumentException("Unknown role: " + role);
      };
    }

    public static UserRole mapToUserRole(SenderType type) {
      return switch (type) {
        case CLIENT -> UserRole.CLIENT;
        case OPERATOR -> UserRole.OPERATOR;
      };
    }

    public SenderType toSenderType() {
      return switch (this) {
        case CLIENT -> SenderType.CLIENT;
        case OPERATOR -> SenderType.OPERATOR;
      };
    }

    @Override
    public String toString() {
      return switch (this) {
        case CLIENT -> "client";
        case OPERATOR -> "operator";
      };
    }
  }

  private MessageSender toMessageSender(UUID senderId, UserRole role) {
    return new MessageSender(role.toSenderType(), senderId);
  }
}
