package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ycyw.users.domain.model.entity.client.Client;
import com.ycyw.users.domain.model.entity.credential.CredentialId;
import com.ycyw.users.domain.model.valueobject.Address;
import com.ycyw.users.domain.model.valueobject.BirthDate;
import com.ycyw.users.domain.model.valueobject.Country;
import com.ycyw.users.domain.model.valueobject.Email;
import com.ycyw.users.domain.port.repository.ClientRepository;
import com.ycyw.users.infrastructure.entity.ClientEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class ClientRepositoryJpaAdapter implements ClientRepository {
  private final ClientJpaRepository repo;

  public ClientRepositoryJpaAdapter(ClientJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public @Nullable Client findByCredentialId(UUID credentialUuid) {
    final var e = repo.findByCredentialId(credentialUuid);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  @Override
  public @Nullable Client find(UUID id) {
    final var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  @Override
  public void save(Client entity) {
    var e = toEntity(entity);
    repo.save(e);
  }

  @Override
  public void update(Client entity) {
    var e = toEntity(entity);
    repo.save(e);
  }

  @Override
  public void delete(Client entity) {
    repo.deleteById(entity.getId());
  }

  @Override
  public @Nullable Client findByEmail(Email email) {
    var e = repo.findByEmailIgnoreCase(email.value());
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  // --- Mapping helpers ---

  private Client toDomain(ClientEntity e) {
    var birthDate = new BirthDate(e.getBirthdate());
    var email = toEmail(e.getEmail());
    var address =
        new Address(
            e.getAddressLine1(),
            e.getAddressLine2(),
            e.getAddressLine3(),
            e.getAddressCity(),
            e.getAddressPostcode(),
            toCountry(e.getAddressCountry()));
    var credentialId = new CredentialId(e.getCredentialId());

    return Client.hydrate(
        e.getId(),
        e.getLastName(),
        e.getFirstName(),
        email,
        e.getPhone(),
        birthDate,
        address,
        credentialId,
        e.getUpdatedAt(),
        e.getDeletedAt());
  }

  private ClientEntity toEntity(Client domain) {
    var e = new ClientEntity();
    e.setId(domain.getId());
    e.setLastName(domain.getLastName());
    e.setFirstName(domain.getFirstName());
    e.setEmail(domain.getEmail().value());
    e.setPhone(domain.getPhone());
    e.setBirthdate(domain.getBirthDate().value());
    e.setAddressLine1(domain.getAddress().line1());
    e.setAddressLine2(domain.getAddress().line2());
    e.setAddressLine3(domain.getAddress().line3());
    e.setAddressCity(domain.getAddress().city());
    e.setAddressPostcode(domain.getAddress().zipCode());
    e.setAddressCountry(domain.getAddress().country().getIsoCode());
    e.setCredentialId(domain.getCredentialId().value());
    e.setUpdatedAt(domain.getUpdatedAt());
    e.setDeletedAt(domain.getDeletedAt());
    return e;
  }

  private Email toEmail(String email) {
    return new Email(email);
  }

  private Country toCountry(String countryCode) {
    return Country.fromCode(countryCode);
  }
}
