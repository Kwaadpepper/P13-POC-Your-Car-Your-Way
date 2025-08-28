package com.ycyw.support.application.service.event.eventsdtos;

public record TypingEvent(String user, String role, String conversation, boolean typing) {}
