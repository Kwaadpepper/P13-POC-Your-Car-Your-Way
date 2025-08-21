package com.ycyw.users.domain.port.service.token;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.valueobject.jwt.AccessTokenClaims;

public interface JwtAccessTokenManager extends TokenManager<JwtAccessToken, AccessTokenClaims> {}
