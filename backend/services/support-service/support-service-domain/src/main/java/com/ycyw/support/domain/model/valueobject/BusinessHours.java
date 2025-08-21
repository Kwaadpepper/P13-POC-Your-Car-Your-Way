package com.ycyw.support.domain.model.valueobject;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

import com.ycyw.shared.ddd.objectvalues.TimeRange;
import com.ycyw.shared.utils.Domain;

public record BusinessHours(Map<DayOfWeek, List<TimeRange>> hours) {
  public BusinessHours {
    hours.forEach(
        (day, ranges) ->
            ranges.forEach(
                range -> Domain.checkDomain(() -> range != null, "TimeRange cannot be null")));
  }
}
