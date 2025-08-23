package com.ycyw.support.domain.model.valueobject.conversation;

import java.util.UUID;

public record MessageSender(SenderType type, UUID id) {}
