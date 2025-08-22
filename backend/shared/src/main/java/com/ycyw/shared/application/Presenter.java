package com.ycyw.shared.application;

public interface Presenter<O, I> {
  /**
   * Transforms the given model into a record for presentation.
   *
   * @param model the model to present
   * @return the presented record
   */
  O present(I model);
}
