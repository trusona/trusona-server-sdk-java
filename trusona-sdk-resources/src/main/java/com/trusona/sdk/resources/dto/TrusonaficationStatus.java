package com.trusona.sdk.resources.dto;

/**
 * Represents all the possible states/outcomes of an authentication request.
 */
public enum TrusonaficationStatus {
  /**
   * The authentication request could not be processed and the user was never issued a challenge.
   */
  INVALID,

  /**
   * The authentication request has not been responded to and has not expired.
   */
  IN_PROGRESS,

  /**
   * The authentication request was rejected by the user.
   */
  REJECTED,

  /**
   * The user accepted the authentication request and exactly met all the security requirements.
   */
  ACCEPTED,

  /**
   * The user accepted the authentication request, but at least one of the security requirements
   * that were ask for was not met by the user.
   */
  ACCEPTED_AT_LOWER_LEVEL,

  /**
   * The user accepted the authentication request and provided more security measures than were required.
   */
  ACCEPTED_AT_HIGHER_LEVEL,

  /**
   * The authentication request was not responded to or timed-out before getting a response.
   */
  EXPIRED,

  /**
   * The authentication request was canceled before it could be acted upon
   */
  CANCELED
}