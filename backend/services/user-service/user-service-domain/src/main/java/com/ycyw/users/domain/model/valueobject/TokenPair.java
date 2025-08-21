package com.ycyw.users.domain.model.valueobject;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.users.domain.model.valueobject.jwt.JwtRefreshToken;

public record TokenPair(JwtAccessToken accessToken, JwtRefreshToken refreshToken) {}
