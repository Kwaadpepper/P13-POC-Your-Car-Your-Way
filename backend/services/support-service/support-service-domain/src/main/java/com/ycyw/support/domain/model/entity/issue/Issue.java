package com.ycyw.support.domain.model.entity.issue;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.utils.Domain;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.model.entity.externals.users.ClientId;
import com.ycyw.support.domain.model.valueobject.IssueStatus;

import org.eclipse.jdt.annotation.Nullable;

public class Issue extends AggregateRoot {
  private final String subject;
  private final String description;
  private final IssueStatus status;
  private final ClientId client;
  private final @Nullable ReservationId reservation;
  private final ZonedDateTime updatedAt;

  private static void validateInvariants(String subject, String description) {
    Domain.checkDomain(() -> !subject.isBlank(), "Subject cannot be blank");
    Domain.checkDomain(() -> !description.isBlank(), "Description cannot be blank");
  }

  public Issue(
      String subject,
      String description,
      IssueStatus status,
      ClientId client,
      @Nullable ReservationId reservation,
      ZonedDateTime updatedAt) {
    super();
    validateInvariants(subject, description);
    this.subject = subject;
    this.description = description;
    this.status = status;
    this.client = client;
    this.reservation = reservation;
    this.updatedAt = updatedAt;
  }

  protected Issue(
      UUID id,
      String subject,
      String description,
      IssueStatus status,
      ClientId client,
      @Nullable ReservationId reservation,
      ZonedDateTime updatedAt) {
    super(id);
    validateInvariants(subject, description);
    this.subject = subject;
    this.description = description;
    this.status = status;
    this.client = client;
    this.reservation = reservation;
    this.updatedAt = updatedAt;
  }

  public static Issue hydrate(
      UUID id,
      String subject,
      String description,
      IssueStatus status,
      ClientId client,
      @Nullable ReservationId reservation,
      ZonedDateTime updatedAt) {
    return new Issue(id, subject, description, status, client, reservation, updatedAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getSubject() {
    return subject;
  }

  public String getDescription() {
    return description;
  }

  public IssueStatus getStatus() {
    return status;
  }

  public ClientId getClient() {
    return client;
  }

  public @Nullable ReservationId getReservation() {
    return reservation;
  }

  @Override
  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Issue that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
