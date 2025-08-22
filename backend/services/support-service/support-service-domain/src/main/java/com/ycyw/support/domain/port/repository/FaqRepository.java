package com.ycyw.support.domain.port.repository;

import java.util.List;

import com.ycyw.shared.ddd.lib.Repository;
import com.ycyw.support.domain.model.entity.faq.Faq;

public interface FaqRepository extends Repository<Faq> {
  public List<Faq> findAll();
}
