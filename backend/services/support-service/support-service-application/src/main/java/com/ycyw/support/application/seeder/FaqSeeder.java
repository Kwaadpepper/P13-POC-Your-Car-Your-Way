package com.ycyw.support.application.seeder;

import org.springframework.stereotype.Component;

import com.ycyw.shared.ddd.lib.UseCaseExecutor;
import com.ycyw.support.domain.model.valueobject.FaqCategory;
import com.ycyw.support.domain.usecase.faq.CreateFaq;

import net.datafaker.Faker;

@Component
public class FaqSeeder implements Seeder {
  private static final int AMOUNT_TO_SEED = 10;

  private final Faker dataFaker;
  private final UseCaseExecutor useCaseExecutor;
  private final CreateFaq.Handler handler;

  public FaqSeeder(Faker dataFaker, UseCaseExecutor useCaseExecutor, CreateFaq.Handler handler) {
    this.dataFaker = dataFaker;
    this.useCaseExecutor = useCaseExecutor;
    this.handler = handler;
  }

  @Override
  public void seed() {
    int i = 0;
    while (i < AMOUNT_TO_SEED) {
      createModel();
      i++;
    }
  }

  private void createModel() {
    var question = dataFaker.lorem().sentence();
    // Ensure it looks like a question
    if (!question.endsWith("?")) {
      question = question.replaceAll("\\.+$", "") + "?";
    }

    var answer = dataFaker.lorem().paragraph();
    var category = dataFaker.commerce().department();

    var useCase = new CreateFaq.CreateInput(question, answer, new FaqCategory(category));

    useCaseExecutor.execute(this.handler, useCase);
  }
}
