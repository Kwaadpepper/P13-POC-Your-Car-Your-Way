package com.ycyw.users.application.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull @NotEmpty String login, @NotNull @NotEmpty String password) {}
