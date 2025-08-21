package com.ycyw.users.application.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

public record OperatorViewDto(
    UUID id,
    String name,
    String email,
    Set<String> roles,
    ZonedDateTime updatedAt,
    @Nullable ZonedDateTime deletedAt) {}
