package com.ycyw.support.domain.model.entity.faq;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;
import com.ycyw.shared.utils.Domain;
import com.ycyw.support.domain.model.valueobject.FaqCategory;

import org.eclipse.jdt.annotation.Nullable;

public class Faq extends AggregateRoot {
  private final String question;
  private final String answer;
  private final FaqCategory category;
  private final ZonedDateTime updatedAt;

  private static void validateInvariants(String question, String answer) {
    Domain.checkDomain(() -> !question.isBlank(), "Question cannot be blank");
    Domain.checkDomain(() -> !answer.isBlank(), "Answer cannot be blank");
  }

  public Faq(String question, String answer, FaqCategory category, ZonedDateTime updatedAt) {
    super();
    validateInvariants(question, answer);
    this.question = question;
    this.answer = answer;
    this.category = category;
    this.updatedAt = updatedAt;
  }

  protected Faq(
      UUID id, String question, String answer, FaqCategory category, ZonedDateTime updatedAt) {
    super(id);
    validateInvariants(question, answer);
    this.question = question;
    this.answer = answer;
    this.category = category;
    this.updatedAt = updatedAt;
  }

  public static Faq hydrate(
      UUID id, String question, String answer, FaqCategory category, ZonedDateTime updatedAt) {
    return new Faq(id, question, answer, category, updatedAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getQuestion() {
    return question;
  }

  public String getAnswer() {
    return answer;
  }

  public FaqCategory getCategory() {
    return category;
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
    if (!(obj instanceof Faq that)) {
      return false;
    }
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
