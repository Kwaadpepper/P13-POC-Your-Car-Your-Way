package com.ycyw.users.infrastructure.adapter.repository.jpa;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

import org.springframework.stereotype.Repository;

import com.ycyw.users.domain.model.entity.credential.Credential;
import com.ycyw.users.domain.model.valueobject.HashedIdentifier;
import com.ycyw.users.domain.model.valueobject.HashedPassword;
import com.ycyw.users.domain.model.valueobject.PartialPassKeysInfo;
import com.ycyw.users.domain.model.valueobject.SsoInfo;
import com.ycyw.users.domain.model.valueobject.SsoProvider;
import com.ycyw.users.domain.model.valueobject.ToptCode;
import com.ycyw.users.domain.port.repository.CredentialRepository;
import com.ycyw.users.infrastructure.entity.CredentialEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class CredentialRepositoryJpaAdapter implements CredentialRepository {

  private final CredentialJpaRepository repo;

  public CredentialRepositoryJpaAdapter(CredentialJpaRepository repo) {
    this.repo = Objects.requireNonNull(repo, "repo");
  }

  @Override
  public @Nullable Credential find(UUID id) {
    var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return repo.findById(id).map(this::toDomain).get();
  }

  @Override
  public @Nullable Credential findByIdentifier(HashedIdentifier identifier) {
    var e = repo.findByHashedIdentifier(identifier.value());
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).get();
  }

  @Override
  public void save(Credential entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(Credential entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(Credential entity) {
    repo.deleteById(entity.getId());
  }

  // --- Mapping helpers ---

  private Credential toDomain(CredentialEntity e) {
    var hashedIdentifier = new HashedIdentifier(e.getHashedIdentifier());
    @Nullable HashedPassword hashedPassword = new HashedPassword(e.getHashedPassword());
    @Nullable SsoInfo ssoInfo = toSsoInfo(e);
    @Nullable String etopt = e.getToptCodeValue();
    @Nullable ToptCode topt = etopt == null ? null : new ToptCode(etopt);
    @Nullable PartialPassKeysInfo passKeyInfo = toPassKeysInfo(e);
    @Nullable ZonedDateTime lastConnection = e.getLastConnection();

    // hydrate to preserve id
    return Credential.hydrate(
        e.getId(), hashedPassword, hashedIdentifier, ssoInfo, topt, passKeyInfo, lastConnection);
  }

  private @Nullable SsoInfo toSsoInfo(CredentialEntity e) {
    @Nullable String ssoId = e.getSsoId();
    @Nullable SsoProvider ssoProvider = toSsoProvider(e.getSsoProvider());
    return (ssoId != null && ssoProvider != null) ? new SsoInfo(ssoId, ssoProvider) : null;
  }

  private @Nullable PartialPassKeysInfo toPassKeysInfo(CredentialEntity e) {
    @Nullable UUID id = e.getPasskeyId();
    @Nullable Vector<Byte> publicKey = toVector(e.getPasskeyPublicKey());
    @Nullable Integer signCount = e.getPasskeySignCount();
    @Nullable String type = e.getPasskeyType();

    return (id != null && signCount != null && publicKey != null && type != null)
        ? new PartialPassKeysInfo(id, publicKey, signCount, type)
        : null;
  }

  private CredentialEntity toEntity(Credential domain) {
    @Nullable SsoInfo ssoInfo = domain.getSsoInfo();
    @Nullable ToptCode toptCode = domain.getToptCode();
    @Nullable PartialPassKeysInfo partialPassKeysInfo = domain.getPartialPassKeysInfo();
    var e = new CredentialEntity();
    e.setId(domain.getId());
    e.setLastConnection(domain.getLastConnectionAt());
    e.setHashedIdentifier(domain.getHashedIdentifier().value());
    e.setHashedPassword(domain.getHashedPassword().value());
    e.setSsoId(ssoInfo != null ? ssoInfo.providerId() : null);
    e.setSsoProvider(ssoInfo != null ? fromSsoProvider(ssoInfo.provider()) : null);
    e.setToptCodeValue(toptCode != null ? toptCode.value() : null);
    e.setPasskeyId(partialPassKeysInfo != null ? partialPassKeysInfo.id() : null);
    e.setPasskeyPublicKey(
        partialPassKeysInfo != null ? fromVector(partialPassKeysInfo.publicKey()) : null);
    e.setPasskeySignCount(partialPassKeysInfo != null ? partialPassKeysInfo.signCount() : null);
    e.setPasskeyType(partialPassKeysInfo != null ? partialPassKeysInfo.type() : null);
    return e;
  }

  private @Nullable Byte[] fromVector(@Nullable Vector<Byte> vector) {
    return vector == null ? null : vector.toArray(new Byte[0]);
  }

  private @Nullable Vector<Byte> toVector(@Nullable Byte[] array) {
    return array == null ? null : new Vector<>(Arrays.asList(array));
  }

  private @Nullable Integer fromSsoProvider(@Nullable SsoProvider provider) {
    return provider == null ? null : provider.getCardinality();
  }

  private @Nullable SsoProvider toSsoProvider(@Nullable Integer providerIdx) {
    return providerIdx == null ? null : SsoProvider.fromCardinality(providerIdx);
  }
}
