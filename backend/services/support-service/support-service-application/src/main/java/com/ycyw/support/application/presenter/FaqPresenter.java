package com.ycyw.support.application.presenter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycyw.shared.application.Presenter;
import com.ycyw.support.application.dto.FaqViewDto;
import com.ycyw.support.domain.usecase.faq.GetAllFaq;

@Component
public class FaqPresenter implements Presenter<List<FaqViewDto>, GetAllFaq.Output> {

  @Override
  public List<FaqViewDto> present(GetAllFaq.Output output) {
    if (output instanceof GetAllFaq.Output.All(var results)) {
      return results.stream().map(this::toDto).toList();
    }
    throw new IllegalArgumentException("Unexpected output type: " + output.getClass());
  }

  private FaqViewDto toDto(GetAllFaq.Output.FaqDto model) {
    return new FaqViewDto(
        model.id(), model.question(), model.answer(), model.category().toString());
  }
}
