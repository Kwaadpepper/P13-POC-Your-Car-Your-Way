package com.ycyw.users.infrastructure.storage;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

public interface KeyStorage {
  void store(String key, String value);

  void store(String key, String value, long ttlInSeconds);

  @Nullable String retrieve(String key);

  void forget(String key);

  List<String> listKeys(String pattern);
}
