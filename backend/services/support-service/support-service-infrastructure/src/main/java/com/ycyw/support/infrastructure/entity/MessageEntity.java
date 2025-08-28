package com.ycyw.support.infrastructure.entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.eclipse.jdt.annotation.Nullable;

@Entity
@Table(schema = "support_context", name = "messages")
public class MessageEntity {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(name = "message", columnDefinition = "text", nullable = false)
  private String message;

  @Column(name = "conversation", columnDefinition = "uuid", nullable = false)
  private UUID conversationId;

  @Column(name = "sender_type", nullable = false)
  private String senderType;

  // ID opérateur OU client (User Context)
  @Column(name = "sender", columnDefinition = "uuid", nullable = false)
  private UUID senderId;

  public MessageEntity() {
    this.id = UUID.randomUUID();
    this.message = "";
    this.conversationId = UUID.randomUUID();
    this.senderType = "";
    this.senderId = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UUID getConversationId() {
    return conversationId;
  }

  public void setConversationId(UUID conversationId) {
    this.conversationId = conversationId;
  }

  public String getSenderType() {
    return senderType;
  }

  public void setSenderType(String senderType) {
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
