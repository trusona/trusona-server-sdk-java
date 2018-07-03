package com.trusona.sdk.resources.exception;

/**
 * An error that occurs when attempting to bind a device to a user that is already bound to a different user. Will not
 * get thrown if the device is already bound to the same user.
 */
public class DeviceAlreadyBoundException extends TrusonaException {
  private static final long serialVersionUID = 1202135856193971503L;

  public DeviceAlreadyBoundException(String message) {
    super(message);
  }
}
