package com.ycyw.support.domain.model.entity.conversation;

import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.utils.Domain;
import com.ycyw.support.domain.model.entity.issue.IssueId;

import org.eclipse.jdt.annotation.Nullable;

public class Conversation extends AggregateRoot {
  private final String subject;
  private final IssueId issue;

  private static void validateInvariants(String subject) {
    Domain.checkDomain(() -> !subject.isBlank(), "Subject cannot be blank");
  }

  public Conversation(String subject, IssueId issue) {
    super();
    validateInvariants(subject);
    this.subject = subject;
    this.issue = issue;
  }

  protected Conversation(UUID id, String subject, IssueId issue) {
    super(id);
    validateInvariants(subject);
    this.subject = subject;
    this.issue = issue;
  }

  public static Conversation hydrate(UUID id, String subject, IssueId issue) {
    return new Conversation(id, subject, issue);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getSubject() {
    return subject;
  }

  public IssueId getIssue() {
    return issue;
  }

  @Override
  public java.time.ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Conversation that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
