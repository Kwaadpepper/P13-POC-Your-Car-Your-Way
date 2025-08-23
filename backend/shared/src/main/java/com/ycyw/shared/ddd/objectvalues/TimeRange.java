package com.ycyw.shared.ddd.objectvalues;

import java.time.LocalTime;

import com.ycyw.shared.utils.Domain;

public record TimeRange(LocalTime start, LocalTime end) {
  public TimeRange {
    Domain.checkDomain(() -> start.isBefore(end), "Start time must be before end time");
  }
}
