package com.ycyw.users.application.security;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;

public record AuthenticatedUser(UUID id, JwtAccessToken jwtAccessToken, String role)
    implements UserDetails {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new Authority(role));
  }

  @Override
  public String getPassword() {
    return jwtAccessToken.value();
  }

  @Override
  public String getUsername() {
    return id.toString();
  }

  public record Authority(String role) implements GrantedAuthority {
    @Override
    public String getAuthority() {
      return role;
    }
  }
}
