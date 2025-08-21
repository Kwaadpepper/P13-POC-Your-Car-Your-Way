package com.ycyw.users.application.dto;

import java.time.LocalDate;

public record ApiErrorDetails(LocalDate timestamp, String message, String uri) {}
