package com.ycyw.users.domain.model.valueobject.jwt;

public record RefreshTokenClaims(RefreshTokenSubject subject, String role) {}
