package com.ycyw.support.infrastructure.adapter.repository.jpa.conversations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.issue.IssueId;
import com.ycyw.support.domain.port.repository.ConversationRepository;
import com.ycyw.support.infrastructure.entity.ConversationEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class ConversationRepositoryJpaAdapter implements ConversationRepository {

  private final ConversationJpaRepository repo;

  public ConversationRepositoryJpaAdapter(ConversationJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Conversation> findAll() {
    return repo.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().map(this::toDomain).toList();
  }

  @Override
  public @Nullable Conversation find(UUID id) {
    final var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).orElseThrow();
  }

  @Override
  public @Nullable Conversation findByIssueId(IssueId issueId) {
    final var e = repo.findByIssueId(issueId.value());
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).orElseThrow();
  }

  @Override
  public void save(Conversation entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(Conversation entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(Conversation entity) {
    repo.deleteById(entity.getId());
  }

  private Conversation toDomain(ConversationEntity e) {
    return Conversation.hydrate(e.getId(), e.getSubject(), new IssueId(e.getIssueId()));
  }

  private ConversationEntity toEntity(Conversation d) {
    final var e = new ConversationEntity();
    e.setId(d.getId());
    e.setSubject(d.getSubject());
    e.setIssueId(d.getIssue().value());
    return e;
  }
}
