package com.ycyw.shared.application;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A presenter interface for transforming a model into a record for output.
 *
 * @param <U> the type of record to present
 * @param <M> the type of model to be presented
 */
public interface Presenter<U extends Record, M extends @Nullable Record> {
  /**
   * Transforms the given model into a record for presentation.
   *
   * @param model the model to present
   * @return the presented record
   */
  U present(M model);
}
