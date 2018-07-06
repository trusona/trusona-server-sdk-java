package com.trusona.sdk.resources.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * A representation of a paired TruCode
 */
public class TruCode extends BaseDto {
  private static final long serialVersionUID = -2395275535009070455L;

  UUID id;
  String identifier;

  /**
   * Get the ID of the TruCode
   *
   * @return the ID as a UUID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Get the identifier that was paired to this TruCode.
   *
   * @return the identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(23421, 6885665, this);
  }
}
