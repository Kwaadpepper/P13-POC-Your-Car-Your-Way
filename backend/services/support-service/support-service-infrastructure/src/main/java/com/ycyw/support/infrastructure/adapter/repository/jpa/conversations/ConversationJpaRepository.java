package com.ycyw.support.infrastructure.adapter.repository.jpa.conversations;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.support.infrastructure.entity.ConversationEntity;

public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, UUID> {
  Optional<ConversationEntity> findByIssueId(UUID issueId);
}
