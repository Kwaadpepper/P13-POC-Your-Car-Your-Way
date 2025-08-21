package com.ycyw.users.application.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record AuthenticableViewDto(
    UUID id,
    String name,
    String role,
    String email,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt) {}
