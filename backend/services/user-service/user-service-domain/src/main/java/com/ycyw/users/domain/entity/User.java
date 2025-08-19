package com.ycyw.users.domain.entity;

import java.util.UUID;

import com.ycyw.shared.ddd.lib.AggregateRoot;

/**
 * Represents a user entity in the system.
 *
 * @see AggregateRoot
 */
public class User extends AggregateRoot {
  private final String email;

  /**
   * Constructs a new User entity with the specified id and email.
   *
   * @param email the email address of the user
   */
  public User(String email) {
    this.email = email;
  }

  @Override
  public UUID getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
}
