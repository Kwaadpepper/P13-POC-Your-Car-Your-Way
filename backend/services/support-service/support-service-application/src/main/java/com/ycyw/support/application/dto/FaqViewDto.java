package com.ycyw.support.application.dto;

import java.util.UUID;

public record FaqViewDto(UUID id, String question, String answer, String category) {}
