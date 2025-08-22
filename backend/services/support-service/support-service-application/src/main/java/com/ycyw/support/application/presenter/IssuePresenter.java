package com.ycyw.support.application.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.support.application.dto.IssueViewDetailsDto;
import com.ycyw.support.domain.usecase.issue.GetAllIssue;

@Component
public class IssuePresenter implements Presenter<List<IssueViewDetailsDto>, GetAllIssue.Output> {

  @Override
  public List<IssueViewDetailsDto> present(GetAllIssue.Output output) {
    if (output instanceof GetAllIssue.Output.All(var results)) {
      return results.stream().map(this::toDto).toList();
    }
    throw new IllegalArgumentException("Unexpected output type: " + output.getClass());
  }

  private IssueViewDetailsDto toDto(GetAllIssue.Output.IssueDto model) {
    return new IssueViewDetailsDto(
        model.id(),
        model.subject(),
        model.description(),
        model.status().name(),
        model.client(),
        model.reservation(),
        model.updatedAt());
  }
}
