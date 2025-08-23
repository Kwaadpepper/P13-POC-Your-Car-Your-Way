package com.ycyw.support.domain.model.entity.externals.reservation;

import java.time.ZonedDateTime;

import com.ycyw.shared.ddd.lib.Entity;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissCode;
import com.ycyw.support.domain.model.entity.externals.agency.Agency;

public record Reservation(
    ReservationId id,
    String status,
    StartingFrom from,
    ArrivingTo to,
    Vehicule vehicule,
    String payment)
    implements Entity {
  public record StartingFrom(Agency agency, ZonedDateTime at) {}

  public record ArrivingTo(Agency agency, ZonedDateTime at) {}

  public record Vehicule(AcrissCode category) {}
}
