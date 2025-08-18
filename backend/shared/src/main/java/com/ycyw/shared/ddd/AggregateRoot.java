package com.ycyw.shared.ddd;

import com.ycyw.shared.utils.UuidV7;
import java.util.UUID;

/**
 * Represents the base class for aggregate roots in a domain-driven design context.
 *
 * <p>Each aggregate root is assigned a unique identifier upon creation using {@link
 * UuidV7#randomUuid()}. Subclasses should extend this class to inherit the unique identity
 * functionality.
 */
public abstract class AggregateRoot {
  protected final UUID id;

  /**
   * Protected constructor for AggregateRoot. Initializes the aggregate root with a randomly
   * generated UUID version 7. This ensures each aggregate root instance has a unique identifier.
   */
  protected AggregateRoot() {
    this.id = UuidV7.randomUuid();
  }

  /**
   * Returns the unique identifier of this aggregate root.
   *
   * @return the UUID of the aggregate root
   */
  public abstract UUID getId();
}
