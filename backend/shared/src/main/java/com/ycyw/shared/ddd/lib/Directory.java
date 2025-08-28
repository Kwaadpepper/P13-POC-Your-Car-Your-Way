package com.ycyw.shared.ddd.lib;

import org.eclipse.jdt.annotation.Nullable;

public interface Directory<E, I> {
  @Nullable E find(I id);
}
