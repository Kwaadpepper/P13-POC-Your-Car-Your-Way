package com.ycyw.users.domain.usecase.user;

import com.ycyw.users.domain.entity.User;
import java.util.UUID;

interface GetUser {
  User execute(UUID userId);
}
