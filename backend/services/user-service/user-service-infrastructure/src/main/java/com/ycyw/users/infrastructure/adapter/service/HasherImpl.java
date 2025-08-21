package com.ycyw.users.infrastructure.adapter.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Service;

import com.ycyw.users.domain.port.service.Hasher;

@Service
public class HasherImpl implements Hasher {

  @Override
  public String hash(String input, HashAlgorithm algorithm) throws HashException {
    // Implementation of hashing logic based on the algorithm
    return switch (algorithm) {
      case SHA256 -> sha256(input);
      case SHA512 -> sha512(input);
    };
  }

  private String sha256(final String base) throws HashException {
    return shaHash(base, "SHA-256");
  }

  private String sha512(final String base) throws HashException {
    return shaHash(base, "SHA-512");
  }

  private String shaHash(final String base, final String algorithm) throws HashException {
    try {
      final MessageDigest digest = MessageDigest.getInstance(algorithm);
      final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
      final StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        final String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new HashException("Error hashing input with SHA-256", e);
    }
  }

  public static class HashException extends RuntimeException {
    public HashException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
