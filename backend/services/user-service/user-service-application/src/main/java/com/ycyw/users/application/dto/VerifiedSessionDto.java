package com.ycyw.users.application.dto;

import java.util.UUID;

public record VerifiedSessionDto(UUID subject, String username, String role) {}
