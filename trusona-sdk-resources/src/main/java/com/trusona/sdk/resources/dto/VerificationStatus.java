package com.trusona.sdk.resources.dto;

/**
 * Represents the possible outcomes of an identity document verification
 */
public enum VerificationStatus {
  /**
   * Verification of the identity document has not been attempted.
   */
  UNVERIFIED,

  /**
   * Verification of the identity document was attempted but failed. Re add the document with the Mobile SDK to retry
   * verification.
   */
  UNVERIFIABLE,

  /**
   * The document was sucessfully verified.
   */
  VERIFIED,

  /**
   * The document failed verification.
   */
  FAILED
}
