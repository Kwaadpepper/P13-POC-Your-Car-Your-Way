package com.ycyw.support.domain.usecase.faq;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.faq.Faq;
import com.ycyw.support.domain.model.valueobject.FaqCategory;
import com.ycyw.support.domain.port.repository.FaqRepository;

public sealed interface GetAllFaq {
  sealed interface Input extends UseCaseInput, GetAllFaq {
    record GetAll() implements Input {}
  }

  sealed interface Output extends UseCaseOutput, GetAllFaq {
    record All(List<FaqDto> results) implements Output {}

    record FaqDto(
        UUID id, String question, String answer, FaqCategory category, ZonedDateTime updatedAt) {}
  }

  final class GetAllFaqHandler implements UseCaseHandler<Input, Output>, GetAllFaq {
    private final FaqRepository faqRepository;

    public GetAllFaqHandler(FaqRepository faqRepository) {
      this.faqRepository = faqRepository;
    }

    @Override
    public Output handle(Input unused) {

      final var entities = faqRepository.findAll();

      return new Output.All(entities.stream().map(this::mapToDto).toList());
    }

    private Output.FaqDto mapToDto(Faq entity) {
      return new Output.FaqDto(
          entity.getId(),
          entity.getQuestion(),
          entity.getAnswer(),
          entity.getCategory(),
          entity.getUpdatedAt());
    }
  }
}
