package com.ycyw.support.infrastructure.storage;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import org.eclipse.jdt.annotation.Nullable;

@Component
public class KeyStorageRedisAdapter implements KeyStorage {
  private final StringRedisTemplate redisTemplate;
  private final ValueOperations<String, String> valueOps;

  public KeyStorageRedisAdapter(StringRedisTemplate redisTemplate) {
    this.redisTemplate = Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
    this.valueOps = this.redisTemplate.opsForValue();
  }

  @Override
  public void store(String key, String value) {
    valueOps.set(key, value);
  }

  @Override
  public void store(String key, String value, long ttlInSeconds) {
    if (ttlInSeconds <= 0) {
      throw new IllegalArgumentException("ttlInSeconds must be positive");
    }
    valueOps.set(key, value, Duration.ofSeconds(ttlInSeconds));
  }

  @Override
  public @Nullable String retrieve(String key) {
    Objects.requireNonNull(key, "key must not be null");
    return valueOps.get(key);
  }

  @Override
  public void forget(String key) {
    Objects.requireNonNull(key, "key must not be null");
    redisTemplate.delete(key);
  }

  @Override
  public java.util.List<String> listKeys(String pattern) {
    @Nullable Set<String> keys = redisTemplate.keys(pattern);

    if (keys == null) {
      throw new IllegalStateException("Redis returned null for keys with pattern: " + pattern);
    }

    return keys.stream().toList();
  }
}
