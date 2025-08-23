package com.ycyw.support.application.dto;

import java.util.UUID;

public record ConversationDto(UUID id, String subject, UUID issue) {}
