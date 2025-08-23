package com.ycyw.support.infrastructure.adapter.client.client;

import com.ycyw.annotation.annotations.Directory;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.BirthDate;
import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.support.domain.model.entity.externals.client.Client;
import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.port.directory.ClientDirectory;
import com.ycyw.support.infrastructure.feignclient.ClientServiceFeignClient;
import com.ycyw.support.infrastructure.feignclient.ClientServiceFeignClient.ClientDto;

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
    } catch (Exception e) {
      logger.error("Error while calling user-service to get client by id: {}", id, e);
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
}
