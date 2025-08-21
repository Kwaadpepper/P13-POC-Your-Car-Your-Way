package com.ycyw.support.infrastructure.adapter.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.support.infrastructure.entity.FaqEntity;

public interface FaqJpaRepository extends JpaRepository<FaqEntity, UUID> {}
