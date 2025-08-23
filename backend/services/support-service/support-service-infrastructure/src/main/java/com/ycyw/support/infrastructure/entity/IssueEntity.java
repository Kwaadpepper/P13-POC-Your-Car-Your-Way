package com.ycyw.support.infrastructure.entity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(schema = "support_context", name = "issues")
public class IssueEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, length = 255)
  private String subject;

  @Column(name = "description", nullable = false, columnDefinition = "text")
  private String description;

  @Column(name = "answer", columnDefinition = "text")
  private @Nullable String answer;

  // smallint -> short
  @JdbcTypeCode(SqlTypes.SMALLINT)
  @Column(name = "status", nullable = false)
  private short status;

  // Référence externe (Customer Context)
  @Column(name = "client", columnDefinition = "uuid", nullable = false)
  private UUID clientId;

  // Référence externe (Reservation Context)
  @Column(name = "reservation", columnDefinition = "uuid")
  private @Nullable UUID reservationId;

  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  public IssueEntity() {
    this.id = UUID.randomUUID();
    this.subject = "";
    this.description = "";
    this.answer = null;
    this.status = 0;
    this.clientId = UUID.randomUUID();
    this.reservationId = null;
    this.updatedAt = ZonedDateTime.now(ZoneId.systemDefault());
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public @Nullable String getAnswer() {
    return answer;
  }

  public void setAnswer(@Nullable String answer) {
    this.answer = answer;
  }

  public short getStatus() {
    return status;
  }

  public void setStatus(short status) {
    this.status = status;
  }

  public UUID getClientId() {
    return clientId;
  }

  public void setClientId(UUID clientId) {
    this.clientId = clientId;
  }

  public @Nullable UUID getReservationId() {
    return reservationId;
  }

  public void setReservationId(@Nullable UUID reservationId) {
    this.reservationId = reservationId;
  }

  public ZonedDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(ZonedDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof IssueEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
