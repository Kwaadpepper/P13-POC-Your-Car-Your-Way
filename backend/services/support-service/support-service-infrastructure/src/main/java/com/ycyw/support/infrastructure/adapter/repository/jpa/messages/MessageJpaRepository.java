package com.ycyw.support.infrastructure.adapter.repository.jpa.messages;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.support.infrastructure.entity.MessageEntity;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, UUID> {
  List<MessageEntity> findByConversationId(UUID conversationId);

  Optional<MessageEntity> findTopByConversationIdOrderByIdDesc(UUID conversationId);
}
