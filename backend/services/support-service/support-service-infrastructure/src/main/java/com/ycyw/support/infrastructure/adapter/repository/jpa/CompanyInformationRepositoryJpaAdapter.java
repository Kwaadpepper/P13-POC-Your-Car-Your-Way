package com.ycyw.support.infrastructure.adapter.repository.jpa;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.HttpUrl;
import com.ycyw.shared.ddd.objectvalues.TimeRange;
import com.ycyw.shared.utils.UuidV7;
import com.ycyw.support.domain.model.entity.company.CompanyInformation;
import com.ycyw.support.domain.model.valueobject.Address;
import com.ycyw.support.domain.model.valueobject.BusinessHours;
import com.ycyw.support.domain.model.valueobject.PhoneNumber;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;
import com.ycyw.support.infrastructure.entity.CompanyInformationEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class CompanyInformationRepositoryJpaAdapter implements CompanyInformationRepository {
  private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");
  private final CompanyInformationJpaRepository repo;

  public CompanyInformationRepositoryJpaAdapter(CompanyInformationJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public @Nullable CompanyInformation find(UUID id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(CompanyInformation entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(CompanyInformation entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(CompanyInformation entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CompanyInformation getInfo() {
    final var all = repo.findAll();
    if (all.isEmpty()) {
      throw new IllegalStateException("No CompanyInformation available");
    }
    @Nullable final CompanyInformationEntity first = all.getFirst();
    return toDomain(first);
  }

  private CompanyInformation toDomain(CompanyInformationEntity e) {
    final var address =
        new Address(
            e.getAddressLine1(),
            e.getAddressLine2(),
            e.getAddressLine3(),
            e.getAddressCity(),
            e.getAddressPostcode(),
            Country.fromCode(e.getAddressCountry()));
    final var phone = new PhoneNumber(e.getSupportPhone());
    final var phoneHours = toBusinessHours(e.getSupportPhoneBusinessHours());
    final var chatHours = toBusinessHours(e.getSupportChatBusinessHours());
    final var email = new Email(e.getSupportEmail());
    final var website = new HttpUrl(URI.create(e.getWebsite()));

    final var syntheticId = UuidV7.randomUuid();

    return CompanyInformation.hydrate(
        syntheticId, address, phone, phoneHours, chatHours, email, website);
  }

  private CompanyInformationEntity toEntity(CompanyInformation d) {
    final var e = new CompanyInformationEntity();
    e.setSupportEmail(d.getEmailSupport().value());
    e.setAddressLine1(d.getCompanyAddress().line1());
    e.setAddressLine2(d.getCompanyAddress().line2());
    e.setAddressLine3(d.getCompanyAddress().line3());
    e.setAddressCity(d.getCompanyAddress().city());
    e.setAddressPostcode(d.getCompanyAddress().zipCode());
    e.setAddressCountry(d.getCompanyAddress().country().getIsoCode());
    e.setSupportPhone(d.getPhoneSupport().value());
    e.setSupportPhoneBusinessHours(fromBusinessHours(d.getPhoneSupportBusinessHours()));
    e.setSupportChatBusinessHours(fromBusinessHours(d.getChatSupportBusinessHours()));
    e.setWebsite(d.getWebsite().toString());
    return e;
  }

  // Convert DB JSON (Map<String, Object>) -> BusinessHours
  private BusinessHours toBusinessHours(@Nullable Map<String, Object> raw) {
    if (raw == null || raw.isEmpty()) {
      return new BusinessHours(Map.of());
    }
    Map<DayOfWeek, TimeRange> result = new EnumMap<>(DayOfWeek.class);
    for (var entry : raw.entrySet()) {
      @Nullable final String key = entry.getKey();
      @Nullable final Object rangeRawObj = entry.getValue();

      DayOfWeek day = parseDay(key);
      if (day == null) {
        // skip unknown or null keys
        continue;
      }

      @Nullable TimeRange range = parseRange(rangeRawObj);

      if (range != null) {
        result.put(day, range);
      }
    }
    return new BusinessHours(Map.copyOf(result));
  }

  private @Nullable DayOfWeek parseDay(@Nullable String key) {
    if (key == null) {
      return null;
    }
    try {
      return DayOfWeek.valueOf(key);
    } catch (IllegalArgumentException ex) {
      // unknown day string
      return null;
    }
  }

  private @Nullable TimeRange parseRange(@Nullable Object rawRangeObj) {
    if (!(rawRangeObj instanceof Map<?, ?> r)) {
      return null;
    }
    @Nullable final Object s = r.get("start");
    @Nullable final Object e = r.get("end");
    if (s == null || e == null) {
      return null;
    }
    final var startStr = String.valueOf(s);
    final var endStr = String.valueOf(e);
    return new TimeRange(LocalTime.parse(startStr, HH_MM), LocalTime.parse(endStr, HH_MM));
  }

  // Convert BusinessHours -> DB JSON (Map<String, Object>)
  private Map<String, Object> fromBusinessHours(@Nullable BusinessHours hours) {
    if (hours == null || hours.hours().isEmpty()) {
      return Map.of();
    }

    Map<String, Object> out = new LinkedHashMap<>();
    hours
        .hours()
        .forEach(
            (day, range) ->
                out.put(
                    day.name(),
                    Map.of(
                        "start", range.start().format(HH_MM),
                        "end", range.end().format(HH_MM))));

    return out;
  }
}
