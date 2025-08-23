package com.ycyw.support.infrastructure.adapter.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.support.infrastructure.entity.CompanyInformationEntity;

/** Uses support_email as technical identifier since the table has no explicit PK. */
public interface CompanyInformationJpaRepository
    extends JpaRepository<CompanyInformationEntity, String> {}
