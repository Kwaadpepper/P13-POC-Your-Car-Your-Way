package com.ycyw.support.domain.port.service;

import java.util.UUID;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;

import org.eclipse.jdt.annotation.Nullable;

public interface SessionVerifyer {
  public @Nullable UserDetails verify(JwtAccessToken jwtAccessToken);

  public record UserDetails(UUID subject, String username, String role) {}
}
