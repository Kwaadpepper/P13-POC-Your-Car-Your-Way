package com.ycyw.support.infrastructure.adapter.repository.jpa.issue;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ycyw.support.infrastructure.entity.IssueEntity;

public interface IssueJpaRepository extends JpaRepository<IssueEntity, UUID> {}
