package com.ycyw.support.domain.port.repository;

import java.util.List;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.support.domain.model.entity.conversation.Conversation;

public interface ConversationRepository extends Repository<Conversation> {
  public List<Conversation> findAll();
}
