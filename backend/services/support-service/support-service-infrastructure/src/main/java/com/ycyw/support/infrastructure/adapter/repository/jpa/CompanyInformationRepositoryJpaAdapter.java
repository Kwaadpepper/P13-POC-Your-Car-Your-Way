package com.ycyw.support.infrastructure.adapter.repository.jpa;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ycyw.shared.ddd.objectvalues.Country;
import com.ycyw.shared.ddd.objectvalues.Email;
import com.ycyw.shared.ddd.objectvalues.HttpUrl;
import com.ycyw.shared.ddd.objectvalues.TimeRange;
import com.ycyw.support.domain.model.entity.company.CompanyInformation;
import com.ycyw.support.domain.model.valueobject.Address;
import com.ycyw.support.domain.model.valueobject.BusinessHours;
import com.ycyw.support.domain.model.valueobject.PhoneNumber;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;
import com.ycyw.support.infrastructure.entity.CompanyInformationEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class CompanyInformationRepositoryJpaAdapter implements CompanyInformationRepository {

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

    // Domain aggregate root requires a UUID; synthesize a stable one from supportEmail
    final var syntheticId =
        java.util.UUID.nameUUIDFromBytes(
            e.getSupportEmail().getBytes(java.nio.charset.StandardCharsets.UTF_8));

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
    Map<DayOfWeek, List<TimeRange>> result = new EnumMap<>(DayOfWeek.class);
    for (var entry : raw.entrySet()) {
      @Nullable final String key = entry.getKey();
      @Nullable final Object rangesRawObj = entry.getValue();

      DayOfWeek day = parseDay(key);
      if (day == null) {
        // skip unknown or null keys
        continue;
      }

      List<TimeRange> ranges = parseRanges(rangesRawObj);
      result.put(day, List.copyOf(ranges));
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

  private List<TimeRange> parseRanges(@Nullable Object rangesRawObj) {
    if (!(rangesRawObj instanceof List<?> rangesList) || rangesList.isEmpty()) {
      return List.of();
    }
    List<TimeRange> ranges = new ArrayList<>();
    for (Object rawRangeObj : rangesList) {
      TimeRange tr = parseRange(rawRangeObj);
      if (tr != null) {
        ranges.add(tr);
      }
    }
    return ranges;
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
    // Stored as ISO-8601 strings -> round-trip with ZonedDateTime
    return new TimeRange(ZonedDateTime.parse(startStr), ZonedDateTime.parse(endStr));
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
            (day, ranges) -> {
              List<Map<String, Object>> encodedRanges = new ArrayList<>();
              if (ranges != null) {
                for (TimeRange tr : ranges) {
                  if (tr == null || tr.start() == null || tr.end() == null) {
                    continue;
                  }
                  // Use ISO-8601 for round-trip with ZonedDateTime.parse(...)
                  encodedRanges.add(
                      Map.of(
                          "start", tr.start().toString(),
                          "end", tr.end().toString()));
                }
              }
              out.put(day.name(), List.copyOf(encodedRanges));
            });

    return out;
  }
}
