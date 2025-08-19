package com.ycyw.users.application.dto;

import java.util.Date;
import java.util.Map;

public record ValidationErrorDetails(
    Date timestamp, String message, Map<String, String> errors, String uri) {}
