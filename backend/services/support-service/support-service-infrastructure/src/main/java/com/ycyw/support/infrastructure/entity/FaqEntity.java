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

@Entity
@Table(schema = "support_context", name = "faqs")
public class FaqEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, length = 255)
  private String question;

  @Column(name = "answer", nullable = false, columnDefinition = "text")
  private String answer;

  @Column(name = "faq_category", nullable = false, length = 255)
  private String category;

  @Column(name = "updated_at", nullable = false)
  private ZonedDateTime updatedAt;

  public FaqEntity() {
    this.id = UUID.randomUUID();
    this.question = "";
    this.answer = "";
    this.category = "";
    this.updatedAt = ZonedDateTime.now(ZoneId.systemDefault());
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
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
    if (!(obj instanceof FaqEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
