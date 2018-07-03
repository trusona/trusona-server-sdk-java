package com.trusona.sdk.resources.exception;

/**
 * An error that can occur when a Trusonafication that requires the user to show an IdentityDocument but
 * the user does not have any.
 */
public class NoIdentityDocumentsException extends TrusonaException {
  private static final long serialVersionUID = -1226997539195151903L;

  public NoIdentityDocumentsException(String message) {
    super(message);
  }

}

