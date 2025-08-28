package com.ycyw.support.domain.port.directory;

import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.Directory;
import com.ycyw.support.domain.model.entity.conversation.ConversationMessage;

import org.eclipse.jdt.annotation.Nullable;

public interface MessageDirectory extends Directory<ConversationMessage, UUID> {
  List<ConversationMessage> findAll(UUID conversationId);

  @Nullable ConversationMessage findLatestMessageForConversation(UUID conversationId);
}
