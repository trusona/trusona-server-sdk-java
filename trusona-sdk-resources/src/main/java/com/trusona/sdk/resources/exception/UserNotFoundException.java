package com.trusona.sdk.resources.exception;

public class UserNotFoundException extends TrusonaException {
  private static final long serialVersionUID = 8308037173463389803L;

  public UserNotFoundException(String message) {
    super(message);
  }
}
