package com.trusona.sdk.resources.exception;

public class InvalidCredentialsException extends TrusonaException {

  private static final long serialVersionUID = -5826997581995153704L;

  public InvalidCredentialsException() {
    super("The token and/or secret you are using are invalid. Contact Trusona to get valid Server SDK credentials.");
  }
}
