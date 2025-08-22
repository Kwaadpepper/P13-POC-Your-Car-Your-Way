package com.ycyw.support.domain.port.repository;

import java.util.List;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.support.domain.model.entity.issue.Issue;

public interface IssueRepository extends Repository<Issue> {
  public List<Issue> findAll();
}
