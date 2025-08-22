package com.ycyw.support.application.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.shared.ddd.objectvalues.Address;
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
        toClientDto(model.client()),
        model.reservation(),
        model.updatedAt());
  }

  private IssueViewDetailsDto.ClientDto toClientDto(
      com.ycyw.support.domain.model.entity.externals.client.Client model) {
    return new IssueViewDetailsDto.ClientDto(
        model.id().value(),
        model.firstName(),
        model.lastName(),
        model.email().value(),
        model.phone().value(),
        model.birthdate().toString(),
        toAddressDto(model.address()));
  }

  private IssueViewDetailsDto.AddressDto toAddressDto(Address model) {
    return new IssueViewDetailsDto.AddressDto(
        model.line1(),
        model.line2(),
        model.line3(),
        model.city(),
        model.zipCode(),
        model.country().getIsoCode());
  }
}
