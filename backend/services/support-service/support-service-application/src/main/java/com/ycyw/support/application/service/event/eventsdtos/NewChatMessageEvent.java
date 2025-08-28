package com.ycyw.support.application.service.event.eventsdtos;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public record NewChatMessageEvent(
    UUID conversationId,
    UUID messageId,
    UUID senderId,
    String userRole,
    String content,
    ZonedDateTime timestamp)
    implements Serializable {}
