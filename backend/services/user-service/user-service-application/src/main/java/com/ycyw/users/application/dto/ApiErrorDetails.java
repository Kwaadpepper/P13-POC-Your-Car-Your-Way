package com.ycyw.users.application.dto;

import java.util.Date;

public record ApiErrorDetails(Date timestamp, String message, String uri) {}
