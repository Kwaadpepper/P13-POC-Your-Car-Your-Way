package com.ycyw.users.domain.port.service.token;

import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenClaims;
import com.ycyw.users.domain.model.valueobject.jwt.RefreshTokenSubject;

import org.eclipse.jdt.annotation.Nullable;

public interface JwtRefreshTokenManager extends TokenManager<JwtRefreshToken, RefreshTokenClaims> {
  @Nullable JwtRefreshToken find(RefreshTokenSubject subject);
}
