package com.ycyw.support.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

public record IssueViewDetailsDto(
    UUID id,
    String subject,
    String description,
    String status,
    UUID client,
    @Nullable UUID reservation,
    ZonedDateTime updatedAt) {}
