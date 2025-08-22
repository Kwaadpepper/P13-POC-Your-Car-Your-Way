package com.ycyw.support.domain.usecase.faq;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.ycyw.shared.ddd.lib.UseCaseHandler;
import com.ycyw.shared.ddd.lib.UseCaseInput;
import com.ycyw.shared.ddd.lib.UseCaseOutput;
import com.ycyw.support.domain.model.entity.faq.Faq;
import com.ycyw.support.domain.model.valueobject.FaqCategory;
import com.ycyw.support.domain.port.repository.FaqRepository;

public sealed interface CreateFaq {
  record CreateInput(String question, String answer, FaqCategory category)
      implements UseCaseInput, CreateFaq {}

  record Created() implements UseCaseOutput, CreateFaq {}

  final class Handler implements UseCaseHandler<CreateInput, Created>, CreateFaq {
    private final FaqRepository faqRepository;

    public Handler(FaqRepository faqRepository) {
      this.faqRepository = faqRepository;
    }

    @Override
    public Created handle(CreateInput usecaseInput) {
      final var question = usecaseInput.question();
      final var answer = usecaseInput.answer();
      final var category = usecaseInput.category();

      final var faq =
          new Faq(question, answer, category, ZonedDateTime.now(ZoneId.systemDefault()));

      faqRepository.save(faq);

      return new Created();
    }
  }
}
