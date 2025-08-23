package com.ycyw.support.infrastructure.entity;

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
@Table(schema = "support_context", name = "messages")
public class MessageEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(name = "message", columnDefinition = "text")
  private @Nullable String message;

  @Column(name = "conversation", columnDefinition = "uuid")
  private @Nullable UUID conversationId;

  // smallint -> short
  @JdbcTypeCode(SqlTypes.SMALLINT)
  @Column(name = "sender_type", nullable = false)
  private short senderType;

  // ID opérateur OU client (User Context)
  @Column(name = "sender", columnDefinition = "uuid", nullable = false)
  private UUID senderId;

  public MessageEntity() {
    this.id = UUID.randomUUID();
    this.message = null;
    this.conversationId = null;
    this.senderType = 0;
    this.senderId = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public @Nullable UUID getConversationId() {
    return conversationId;
  }

  public void setConversationId(@Nullable UUID conversationId) {
    this.conversationId = conversationId;
  }

  public short getSenderType() {
    return senderType;
  }

  public void setSenderType(short senderType) {
    this.senderType = senderType;
  }

  public UUID getSenderId() {
    return senderId;
  }

  public void setSenderId(UUID senderId) {
    this.senderId = senderId;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof MessageEntity that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
