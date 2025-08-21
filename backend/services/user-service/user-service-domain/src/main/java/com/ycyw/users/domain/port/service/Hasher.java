package com.ycyw.users.domain.port.service;

public interface Hasher {
  String hash(String input, HashAlgorithm algorithm);

  public enum HashAlgorithm {
    SHA256,
    SHA512
  }
}
