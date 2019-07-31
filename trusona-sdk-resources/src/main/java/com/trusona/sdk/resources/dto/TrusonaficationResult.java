package com.trusona.sdk.resources.dto;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.UUID;

/**
 * A representation of an authentication request that has completed.
 */
public class TrusonaficationResult extends BaseDto {

  private static final long serialVersionUID = 800826708432719459L;

  private final UUID trusonaficationId;
  private final TrusonaficationStatus status;
  private final String userIdentifier;
  private final Date expiresAt;
  private final String boundUserIdentifier;

  public TrusonaficationResult(UUID trusonaficationId, TrusonaficationStatus status, String userIdentifier, Date expiresAt, String boundUserIdentifier) {
    this.trusonaficationId = trusonaficationId;
    this.status = status;
    this.userIdentifier = userIdentifier;
    this.expiresAt = expiresAt != null ? new Date(expiresAt.getTime()) : null;
    this.boundUserIdentifier = boundUserIdentifier;
  }

  /**
   * Gets the trusonafication id of the authentication request that was created.
   *
   * @return the trusonafication id.
   */
  public UUID getTrusonaficationId() {
    return trusonaficationId;
  }

  /**
   * Gets the status of the authentication request. The status can be checked if more information is needed than the true/false
   * response from calling the {@link TrusonaficationResult#isSuccessful()} method.
   *
   * @return the trusonafication status.
   */
  public TrusonaficationStatus getStatus() {
    return status;
  }

  /**
   * @return the user's identifier.
   * @deprecated The identifier of the user that responded to the authentication request. May be populated even if the user
   * didn't meet all the security requirements, so it is important check the result of the {@link
   * TrusonaficationResult#isSuccessful()} method before granting access to the user.
   */
  @Deprecated
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Returns true if the user met or exceeded the security requirements of the authentication request. Otherwise, returns
   * false.
   *
   * @return whether the trusonafication was successful.
   */
  public boolean isSuccessful() {
    return status == TrusonaficationStatus.ACCEPTED || status == TrusonaficationStatus.ACCEPTED_AT_HIGHER_LEVEL;
  }

  /**
   * The time at which the original Trusonafication would have expired.
   *
   * @return the expiration time
   */
  public Date getExpiresAt() {
    return expiresAt != null ? new Date(expiresAt.getTime()) : null;
  }

  /**
   * The user identifier bound between the user who responded to the Trusonafication and the Relying Party that created the
   * Trusonafication
   */
  public String getBoundUserIdentifier() {
    return boundUserIdentifier;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(2255, 299837, this);
  }
}
