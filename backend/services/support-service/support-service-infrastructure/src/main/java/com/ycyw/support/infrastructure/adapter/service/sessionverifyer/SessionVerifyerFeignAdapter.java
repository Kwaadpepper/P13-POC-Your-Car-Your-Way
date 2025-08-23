package com.ycyw.support.infrastructure.adapter.service.sessionverifyer;

import org.springframework.stereotype.Service;

import com.ycyw.shared.ddd.objectvalues.JwtAccessToken;
import com.ycyw.support.domain.port.service.SessionVerifyer;
import com.ycyw.support.infrastructure.feignclient.SessionVerifyerFeignClient;
import com.ycyw.support.infrastructure.feignclient.SessionVerifyerFeignClient.UserDetailsDto;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SessionVerifyerFeignAdapter implements SessionVerifyer {
  private static final Logger logger = LoggerFactory.getLogger(SessionVerifyerFeignAdapter.class);
  private final SessionVerifyerFeignClient feignClient;

  public SessionVerifyerFeignAdapter(SessionVerifyerFeignClient feignClient) {
    this.feignClient = feignClient;
  }

  @Override
  public @Nullable UserDetails verify(JwtAccessToken jwtAccessToken) {
    try {
      @Nullable final UserDetailsDto dto = feignClient.verify(jwtAccessToken);

      if (dto == null) {
        return null;
      }

      return new UserDetails(dto.subject(), dto.username(), dto.role());
    } catch (Exception e) {
      logger.error("Error while verifying session for token: {}", jwtAccessToken, e);
      return null;
    }
  }
}
