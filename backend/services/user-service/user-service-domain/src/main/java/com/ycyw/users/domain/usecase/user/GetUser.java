package com.ycyw.users.domain.usecase.user;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCase;

record GetUser(UUID userId) implements UseCase {}
