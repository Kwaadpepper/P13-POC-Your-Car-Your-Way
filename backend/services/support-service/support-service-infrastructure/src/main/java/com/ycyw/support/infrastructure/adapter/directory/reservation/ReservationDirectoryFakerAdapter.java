package com.ycyw.support.infrastructure.adapter.directory.reservation;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import com.ycyw.annotation.annotations.Directory;
import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.Coordinates;
import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.PhoneNumber;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissCategory;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissCode;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissFuelAc;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissTransmission;
import com.ycyw.shared.ddd.objectvalues.acriss.AcrissType;
import com.ycyw.support.domain.model.entity.externals.agency.Agency;
import com.ycyw.support.domain.model.entity.externals.reservation.Reservation;
import com.ycyw.support.domain.model.entity.externals.reservation.Reservation.ArrivingTo;
import com.ycyw.support.domain.model.entity.externals.reservation.Reservation.StartingFrom;
import com.ycyw.support.domain.model.entity.externals.reservation.Reservation.Vehicule;
import com.ycyw.support.domain.model.entity.externals.reservation.ReservationId;
import com.ycyw.support.domain.port.directory.ReservationDirectory;

import net.datafaker.Faker;
import org.eclipse.jdt.annotation.Nullable;

@Directory
public class ReservationDirectoryFakerAdapter implements ReservationDirectory {
  private final Faker faker;

  public ReservationDirectoryFakerAdapter(Faker faker) {
    this.faker = faker;
  }

  @Override
  public @Nullable Reservation findById(ReservationId id) {
    final var fakeNumber = faker.number();
    final var fakeBoolean = faker.bool();
    final var fakeOption = faker.options();

    final Agency fromAgency = generateAgency();
    final StartingFrom from =
        new StartingFrom(fromAgency, generateFutureDate(fakeNumber.numberBetween(1, 10)));

    final Agency toAgency = fakeBoolean.bool() ? generateAgency() : fromAgency;
    final ArrivingTo to =
        new ArrivingTo(toAgency, generateFutureDate(fakeNumber.numberBetween(11, 20)));

    final String status = fakeOption.option("PENDING", "CONFIRMED", "CANCELLED");

    final var vehicule = new Vehicule(generateAcrissCode());
    final var payment = fakeOption.option("PENDING", "CONFIRMED", "REFUSED");

    return new Reservation(id, status, from, to, vehicule, payment);
  }

  private AcrissCode generateAcrissCode() {
    final var fakeOption = faker.options();

    final @Nullable AcrissCategory category = fakeOption.option(AcrissCategory.class);
    final @Nullable AcrissType type = fakeOption.option(AcrissType.class);
    final @Nullable AcrissTransmission transmission = fakeOption.option(AcrissTransmission.class);
    final @Nullable AcrissFuelAc fuelAc = fakeOption.option(AcrissFuelAc.class);
    return new AcrissCode(
        "%c%c%c%c".formatted(category.code(), type.code(), transmission.code(), fuelAc.code()));
  }

  private Agency generateAgency() {
    return new Agency(
        faker.company().name(),
        new PhoneNumber(faker.phoneNumber().phoneNumber()),
        new Email(faker.internet().emailAddress()),
        new Address(
            faker.address().streetAddress(),
            null,
            null,
            faker.address().cityName(),
            faker.address().zipCode(),
            Country.FRANCE),
        new Coordinates(
            Double.parseDouble(faker.address().latitude()),
            Double.parseDouble(faker.address().longitude())));
  }

  private ZonedDateTime generateFutureDate(int daysAhead) {
    return faker
        .timeAndDate()
        .future(daysAhead, TimeUnit.DAYS)
        .atZone(java.time.ZoneId.systemDefault());
  }
}
