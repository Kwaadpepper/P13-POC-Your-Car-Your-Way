package com.ycyw.support.domain.model.valueobject;

import java.time.DayOfWeek;
import java.util.Map;

import com.ycyw.shared.ddd.objectvalues.TimeRange;

public record BusinessHours(Map<DayOfWeek, TimeRange> hours) {}
