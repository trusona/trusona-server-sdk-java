package com.trusona.sdk.resources.exception;

public class InvalidCredentialsException extends RuntimeException {
  private static final long serialVersionUID = -5826997581995153704L;

  public InvalidCredentialsException(String message) {
    super(message);
  }
}
