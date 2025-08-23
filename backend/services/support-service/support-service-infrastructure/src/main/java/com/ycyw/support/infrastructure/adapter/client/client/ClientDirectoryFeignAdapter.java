package com.ycyw.support.infrastructure.adapter.client.client;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.ycyw.annotation.annotations.Directory;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.BirthDate;
import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.port.directory.ClientDirectory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Directory
public class ClientDirectoryFeignAdapter implements ClientDirectory {
  private final Logger logger = LoggerFactory.getLogger(ClientDirectoryFeignAdapter.class);
  private final ClientServiceFeignClient client;

  public ClientDirectoryFeignAdapter(ClientServiceFeignClient client) {
    this.client = client;
  }

  @Override
  public @Nullable Client findById(ClientId id) {
    try {
      ClientDto payload = client.getClientByUuidOrEmail(id.value().toString());

      if (payload == null) {
        return null;
      }

      return map(payload);
    } catch (Exception ex) {
      logger.error("Error while calling user-service to get client by id: {}", id, ex);
      return null;
    }
  }

  private Client map(ClientDto dto) {
    final var dtoAddress = dto.address();

    final var clientId = new ClientId(dto.id());
    final var birthDate = new BirthDate(dto.birthDate());
    final var country = Country.of(dtoAddress.country());
    final var phoneNumber = new PhoneNumber(dto.phone());
    final var email = new Email(dto.email());
    final var address =
        new Address(
            dtoAddress.line1(),
            dtoAddress.line2(),
            dtoAddress.line3(),
            dtoAddress.city(),
            dtoAddress.zipCode(),
            country);

    return new Client(
        clientId, dto.lastName(), dto.firstName(), email, phoneNumber, birthDate, address);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ClientDto(
      UUID id,
      String lastName,
      String firstName,
      String email,
      String phone,
      LocalDate birthDate,
      AddressDto address,
      ZonedDateTime updatedAt,
      @Nullable ZonedDateTime deletedAt) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AddressDto(
      String line1,
      @Nullable String line2,
      @Nullable String line3,
      String city,
      String zipCode,
      String country) {}
}
