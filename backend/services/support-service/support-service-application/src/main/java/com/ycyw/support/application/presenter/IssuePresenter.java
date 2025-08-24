package com.ycyw.support.application.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.support.application.dto.IssueViewDetailsDto;
import com.ycyw.support.application.dto.IssueViewDetailsDto.ReservationDto;
import com.ycyw.support.domain.model.entity.externals.agency.Agency;
import com.ycyw.support.domain.usecase.issue.GetAllIssue.Output;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class IssuePresenter implements Presenter<List<IssueViewDetailsDto>, Output> {

  @Override
  public List<IssueViewDetailsDto> present(Output output) {
    if (output instanceof Output.All(var results)) {
      return results.stream().map(this::toDto).toList();
    }
    throw new IllegalArgumentException("Unexpected output type: " + output.getClass());
  }

  private IssueViewDetailsDto toDto(Output.IssueDto model) {
    return new IssueViewDetailsDto(
        model.id(),
        model.subject(),
        model.description(),
        model.description(),
        model.status().name(),
        toClientDto(model.client()),
        toReservationDto(model.reservation()),
        model.conversation(),
        model.updatedAt());
  }

  private IssueViewDetailsDto.ClientDto toClientDto(Output.ClientDto model) {
    return new IssueViewDetailsDto.ClientDto(
        model.id().value(),
        model.firstName(),
        model.lastName(),
        model.email().value(),
        model.phone().value(),
        model.birthdate().value().toString(),
        toAddressDto(model.address()));
  }

  private @Nullable ReservationDto toReservationDto(Output.@Nullable ReservationDto model) {
    if (model == null) {
      return null;
    }

    final var vehicule = model.vehicule();
    return new ReservationDto(
        model.id(),
        model.status(),
        toStartingFromDto(model.from()),
        toArrivingToDto(model.to()),
        new ReservationDto.VehiculeDto(vehicule.category().value()),
        model.payment());
  }

  private IssueViewDetailsDto.AddressDto toAddressDto(Address address) {
    return new IssueViewDetailsDto.AddressDto(
        address.line1(),
        address.line2(),
        address.line3(),
        address.city(),
        address.zipCode(),
        address.country().getIsoCode());
  }

  private IssueViewDetailsDto.ReservationDto.StartingFromDto toStartingFromDto(
      Output.ReservationDto.StartingFromDto model) {
    return new ReservationDto.StartingFromDto(toAgencyDto(model.agency()), model.at());
  }

  private IssueViewDetailsDto.ReservationDto.ArrivingToDto toArrivingToDto(
      Output.ReservationDto.ArrivingToDto model) {
    return new ReservationDto.ArrivingToDto(toAgencyDto(model.agency()), model.at());
  }

  private ReservationDto.AgencyDto toAgencyDto(Agency model) {
    final var coordinates = model.coordinates();
    return new ReservationDto.AgencyDto(
        model.label(),
        model.phone().value(),
        model.email().value(),
        toAddressDto(model.address()),
        new ReservationDto.CoordinatesDto(coordinates.lat(), coordinates.lng()));
  }
}
