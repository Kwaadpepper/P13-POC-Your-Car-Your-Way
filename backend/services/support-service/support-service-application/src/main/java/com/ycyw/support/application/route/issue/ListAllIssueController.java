package com.ycyw.support.application.route.issue;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.application.dto.IssueViewDetailsDto;
import com.ycyw.support.application.presenter.IssuePresenter;
import com.ycyw.support.domain.usecase.issue.GetAllIssue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ListAllIssueController {
  private final UseCaseExecutor useCaseExecutor;
  private final GetAllIssue.Handler useCaseHandler;
  private final IssuePresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(ListAllIssueController.class);

  public ListAllIssueController(
      UseCaseExecutor useCaseExecutor,
      GetAllIssue.Handler useCaseHandler,
      IssuePresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.useCaseHandler = useCaseHandler;
    this.presenter = presenter;
  }

  @GetMapping("/issues")
  public List<IssueViewDetailsDto> getCompanyInfo() {

    final var output = useCaseExecutor.execute(useCaseHandler, new GetAllIssue.Input.GetAll());

    logger.info("Issue details retrieved: {}", output);

    return presenter.present(output);
  }
}
