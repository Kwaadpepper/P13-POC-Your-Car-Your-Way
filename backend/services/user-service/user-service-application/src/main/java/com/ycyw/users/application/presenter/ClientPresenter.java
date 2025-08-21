package com.ycyw.users.application.presenter;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.users.application.dto.ClientViewDto;
import com.ycyw.users.application.exception.exceptions.ResourceNotFoundException;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.usecase.client.FindClient;

@Component
public class ClientPresenter implements Presenter<ClientViewDto, FindClient.Output> {

  @Override
  public ClientViewDto present(FindClient.Output model) {
    if (model instanceof FindClient.Output.FoundClient foundClient) {
      return toDto(foundClient);
    }
    throw new ResourceNotFoundException("Client not found");
  }

  private ClientViewDto toDto(FindClient.Output.FoundClient model) {
    return new ClientViewDto(
        model.id(),
        model.lastName(),
        model.firstName(),
        model.email().value(),
        model.phone(),
        model.birthDate().value(),
        toAddressDto(model.address()),
        model.updatedAt(),
        model.deletedAt());
  }

  private ClientViewDto.AddressViewDto toAddressDto(Address address) {
    return new ClientViewDto.AddressViewDto(
        address.line1(),
        address.line2(),
        address.line3(),
        address.city(),
        address.zipCode(),
        address.country().getIsoCode());
  }
}
