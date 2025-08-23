package com.ycyw.support.infrastructure.adapter.repository.inmemory;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.ycyw.shared.ddd.objectvalues.Address;
import com.ycyw.shared.ddd.objectvalues.TimeRange;
import com.ycyw.support.domain.model.entity.company.CompanyInformation;
import com.ycyw.support.domain.model.valueobject.BusinessHours;
import com.ycyw.support.domain.port.repository.CompanyInformationRepository;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyInformationRepositoryInMemory implements CompanyInformationRepository {
  private static final Logger logger =
      LoggerFactory.getLogger(CompanyInformationRepositoryInMemory.class);
  private final Map<UUID, CompanyInformation> store = new ConcurrentHashMap<>();

  @Override
  public @Nullable CompanyInformation find(UUID id) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void save(CompanyInformation entity) {
    store.put(entity.getId(), entity);
    logger.debug("Saved CompanyInformation with id {}", entity.getId());
  }

  @Override
  public void update(CompanyInformation entity) {
    store.put(entity.getId(), entity);
    logger.debug("Updated CompanyInformation with id {}", entity.getId());
  }

  @Override
  public void delete(CompanyInformation entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CompanyInformation getInfo() {
    @Nullable final CompanyInformation info = store.values().stream().findFirst().orElseThrow();

    logger.debug("getInfo -> CompanyInformation id={}", info.getId());
    return clone(info);
  }

  private CompanyInformation clone(CompanyInformation entity) {
    return CompanyInformation.hydrate(
        entity.getId(),
        clone(entity.getCompanyAddress()),
        entity.getPhoneSupport(),
        clone(entity.getPhoneSupportBusinessHours()),
        clone(entity.getChatSupportBusinessHours()),
        entity.getEmailSupport(),
        entity.getWebsite());
  }

  private Address clone(Address entity) {
    return new Address(
        entity.line1(),
        entity.line2(),
        entity.line3(),
        entity.city(),
        entity.zipCode(),
        entity.country());
  }

  private BusinessHours clone(BusinessHours entity) {
    Map<DayOfWeek, TimeRange> hoursCopy =
        entity.hours().entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (a, b) -> a,
                    () -> new EnumMap<DayOfWeek, TimeRange>(DayOfWeek.class)));

    return new BusinessHours(new EnumMap<>(hoursCopy));
  }
}
