package com.ycyw.support.domain.port.repository;

import java.util.List;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.support.domain.model.entity.conversation.Conversation;
import com.ycyw.support.domain.model.entity.issue.IssueId;

import org.eclipse.jdt.annotation.Nullable;

public interface ConversationRepository extends Repository<Conversation> {
  public List<Conversation> findAll();

  public @Nullable Conversation findByIssueId(IssueId issueId);
}
