package com.ycyw.users.application.dto;

import java.time.LocalDate;
import java.util.Map;

public record ValidationErrorDetails(
    LocalDate timestamp, String message, Map<String, String> errors, String uri) {}
