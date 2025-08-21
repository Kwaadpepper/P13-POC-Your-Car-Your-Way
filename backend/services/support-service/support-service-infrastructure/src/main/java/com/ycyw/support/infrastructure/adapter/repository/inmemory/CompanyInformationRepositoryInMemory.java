package com.ycyw.support.infrastructure.adapter.repository.inmemory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.ycyw.support.domain.model.entity.company.CompanyInformation;
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
    return info;
  }
}
