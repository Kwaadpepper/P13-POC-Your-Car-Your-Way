package com.ycyw.shared.application;

public interface Presenter<U extends Record, M> {
  /**
   * Transforms the given model into a record for presentation.
   *
   * @param model the model to present
   * @return the presented record
   */
  U present(M model);
}
