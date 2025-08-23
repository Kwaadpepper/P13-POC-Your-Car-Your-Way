package com.ycyw.support.domain.port.directory;

import com.ycyw.support.domain.model.entity.externals.reservation.Reservation;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;

import org.eclipse.jdt.annotation.Nullable;

public interface ReservationDirectory {
  @Nullable Reservation findById(ReservationId id);
}
