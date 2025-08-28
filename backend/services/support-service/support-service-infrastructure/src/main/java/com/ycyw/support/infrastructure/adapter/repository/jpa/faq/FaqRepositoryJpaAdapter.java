package com.ycyw.support.infrastructure.adapter.repository.jpa.faq;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.ycyw.support.domain.model.entity.faq.Faq;
import com.ycyw.support.domain.model.valueobject.FaqCategory;
import com.ycyw.support.domain.port.repository.FaqRepository;
import com.ycyw.support.infrastructure.entity.FaqEntity;

import org.eclipse.jdt.annotation.Nullable;

@Repository
public class FaqRepositoryJpaAdapter implements FaqRepository {

  private final FaqJpaRepository repo;

  public FaqRepositoryJpaAdapter(FaqJpaRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Faq> findAll() {
    return repo.findAll().stream().map(this::toDomain).toList();
  }

  @Override
  public @Nullable Faq find(UUID id) {
    final var e = repo.findById(id);
    if (e.isEmpty()) {
      return null;
    }
    return e.map(this::toDomain).orElseThrow();
  }

  @Override
  public void save(Faq entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void update(Faq entity) {
    repo.save(toEntity(entity));
  }

  @Override
  public void delete(Faq entity) {
    repo.deleteById(entity.getId());
  }

  private Faq toDomain(FaqEntity e) {
    return Faq.hydrate(
        e.getId(),
        e.getQuestion(),
        e.getAnswer(),
        new FaqCategory(e.getCategory()),
        e.getUpdatedAt());
  }

  private FaqEntity toEntity(Faq d) {
    final var e = new FaqEntity();
    e.setId(d.getId());
    e.setQuestion(d.getQuestion());
    e.setAnswer(d.getAnswer());
    e.setCategory(d.getCategory().value());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
