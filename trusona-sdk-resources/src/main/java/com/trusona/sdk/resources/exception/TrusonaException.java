package com.trusona.sdk.resources.exception;

/**
 * Represents either a generic error that occurred within the Trusona Server SDK, or the superclass of a more specific
 * error. The consumer may choose if they want to handle specific exceptions differently or handle all Trusona SDK
 * errors the same.
 */
public class TrusonaException extends Exception {
  private static final long serialVersionUID = -4534471552051985760L;

  public TrusonaException(String message) {
    super(message);
  }

  public TrusonaException(String message, Throwable cause) {
    super(message, cause);
  }
}
