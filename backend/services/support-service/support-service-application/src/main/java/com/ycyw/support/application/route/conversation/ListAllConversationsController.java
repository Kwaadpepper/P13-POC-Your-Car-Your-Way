package com.ycyw.support.application.route.conversation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.application.dto.ConversationDto;
import com.ycyw.support.application.presenter.ConversationPresenter;
import com.ycyw.support.domain.usecase.conversation.GetAllConversation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ListAllConversationsController {
  private final UseCaseExecutor useCaseExecutor;
  private final GetAllConversation.Handler useCaseHandler;
  private final ConversationPresenter presenter;
  private static final Logger logger =
      LoggerFactory.getLogger(ListAllConversationsController.class);

  public ListAllConversationsController(
      UseCaseExecutor useCaseExecutor,
      GetAllConversation.Handler useCaseHandler,
      ConversationPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.useCaseHandler = useCaseHandler;
    this.presenter = presenter;
  }

  @GetMapping("/conversations")
  public List<ConversationDto> getCompanyInfo() {

    final var output =
        useCaseExecutor.execute(useCaseHandler, new GetAllConversation.Input.GetAll());

    logger.info("Conversations retrieved: {}", output);

    return presenter.present(output);
  }
}
