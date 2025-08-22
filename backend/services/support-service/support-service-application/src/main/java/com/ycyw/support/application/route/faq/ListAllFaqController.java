package com.ycyw.support.application.route.faq;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.application.dto.FaqViewDto;
import com.ycyw.support.application.presenter.FaqPresenter;
import com.ycyw.support.domain.usecase.faq.GetAllFaq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ListAllFaqController {
  private final UseCaseExecutor useCaseExecutor;
  private final GetAllFaq.Handler useCaseHandler;
  private final FaqPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(ListAllFaqController.class);

  public ListAllFaqController(
      UseCaseExecutor useCaseExecutor, GetAllFaq.Handler useCaseHandler, FaqPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.useCaseHandler = useCaseHandler;
    this.presenter = presenter;
  }

  @GetMapping("/faqs")
  public List<FaqViewDto> getCompanyInfo() {

    final var output = useCaseExecutor.execute(useCaseHandler, new GetAllFaq.Input.GetAll());

    logger.info("Company info retrieved: {}", output);

    return presenter.present(output);
  }
}
