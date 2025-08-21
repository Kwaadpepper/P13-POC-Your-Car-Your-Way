package com.ycyw.users.domain.model.valueobject.jwt;

public record AccessTokenClaims(AccessTokenSubject subject, String role) {}
