package com.ycyw.support.domain.port.repository;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.support.domain.model.entity.company.CompanyInformation;

public interface CompanyInformationRepository extends Repository<CompanyInformation> {
  public CompanyInformation getInfo();
}
