package com.ycyw.support.application.route.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.application.dto.CompanyInfoViewDto;
import com.ycyw.support.application.presenter.CompanyInfoPresenter;
import com.ycyw.support.domain.usecase.company.GetCompanyInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class CompanyInfoController {
  private final UseCaseExecutor useCaseExecutor;
  private final GetCompanyInfo.Handler useCaseHandler;
  private final CompanyInfoPresenter presenter;
  private static final Logger logger = LoggerFactory.getLogger(CompanyInfoController.class);

  public CompanyInfoController(
      UseCaseExecutor useCaseExecutor,
      GetCompanyInfo.Handler useCaseHandler,
      CompanyInfoPresenter presenter) {
    this.useCaseExecutor = useCaseExecutor;
    this.useCaseHandler = useCaseHandler;
    this.presenter = presenter;
  }

  @GetMapping("/company/info")
  public CompanyInfoViewDto getCompanyInfo() {

    final var output = useCaseExecutor.execute(useCaseHandler, new GetCompanyInfo.Input.Get());

    logger.info("Company info retrieved: {}", output);

    return presenter.present(output);
  }
}
