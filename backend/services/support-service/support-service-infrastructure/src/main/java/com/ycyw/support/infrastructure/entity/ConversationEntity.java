package com.ycyw.support.infrastructure.entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;

@Entity
@Table(schema = "support_context", name = "conversations")
public class ConversationEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, length = 255)
  private String subject;

  // Une conversation par issue (unique en DB)
  @Column(name = "issue", columnDefinition = "uuid", nullable = false, unique = true)
  private UUID issueId;

  public ConversationEntity() {
    this.id = UUID.randomUUID();
    this.subject = "";
    this.issueId = UUID.randomUUID();
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

  public UUID getIssueId() {
    return issueId;
  }

  public void setIssueId(UUID issueId) {
    this.issueId = issueId;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ConversationEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
