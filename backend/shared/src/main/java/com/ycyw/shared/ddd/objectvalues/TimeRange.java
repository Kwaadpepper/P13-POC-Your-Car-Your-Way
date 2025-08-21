package com.ycyw.shared.ddd.objectvalues;

import java.time.ZonedDateTime;

import com.ycyw.shared.utils.Domain;

public record TimeRange(ZonedDateTime start, ZonedDateTime end) {
  public TimeRange {
    Domain.checkDomain(() -> start.isBefore(end), "Start time must be before end time");
  }
}
