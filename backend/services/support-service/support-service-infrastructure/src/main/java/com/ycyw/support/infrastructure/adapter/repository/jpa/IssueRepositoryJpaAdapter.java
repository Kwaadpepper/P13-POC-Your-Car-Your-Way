package com.ycyw.support.infrastructure.adapter.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ycyw.support.domain.model.entity.externals.client.ClientId;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.issue.Issue;
import com.ycyw.support.domain.model.valueobject.IssueStatus;
import com.ycyw.support.domain.port.repository.IssueRepository;
import com.ycyw.support.infrastructure.entity.IssueEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class IssueRepositoryJpaAdapter implements IssueRepository {

  private final IssueJpaRepository repo;

  public IssueRepositoryJpaAdapter(IssueJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Issue> findAll() {
    return repo.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public @Nullable Issue find(UUID id) {
    final var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).orElseThrow();
  }

  @Override
  public void save(Issue entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(Issue entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(Issue entity) {
    repo.deleteById(entity.getId());
  }

  private Issue toDomain(IssueEntity e) {
    final var status = fromShort(e.getStatus());
    final var client = new ClientId(e.getClientId());
    final var reservationKey = e.getReservationId();
    final @Nullable ReservationId reservation =
        reservationKey == null ? null : new ReservationId(reservationKey);
    return Issue.hydrate(
        e.getId(),
        e.getSubject(),
        e.getDescription(),
        status,
        client,
        reservation,
        e.getUpdatedAt());
  }

  private IssueEntity toEntity(Issue d) {
    final var reservation = d.getReservation();
    final var e = new IssueEntity();
    e.setId(d.getId());
    e.setSubject(d.getSubject());
    e.setDescription(d.getDescription());
    e.setAnswer(null);
    e.setStatus(toShort(d.getStatus()));
    e.setClientId(d.getClient().value());
    e.setReservationId(reservation == null ? null : reservation.value());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }

  private short toShort(IssueStatus status) {
    return switch (status) {
      case OPENED -> 0;
      case CLOSED -> 1;
      case RESOLVED -> 2;
    };
  }

  private IssueStatus fromShort(short code) {
    return switch (code) {
      case 0 -> IssueStatus.OPENED;
      case 1 -> IssueStatus.CLOSED;
      case 2 -> IssueStatus.RESOLVED;
      default -> IssueStatus.OPENED;
    };
  }
}
