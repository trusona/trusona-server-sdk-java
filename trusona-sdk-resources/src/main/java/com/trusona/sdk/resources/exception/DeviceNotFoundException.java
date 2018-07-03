package com.trusona.sdk.resources.exception;

/**
 * An error that occurs when the action being taken requires a device to exist, but it cannot be found.
 */
public class DeviceNotFoundException extends TrusonaException {
  private static final long serialVersionUID = 507551471121269967L;

  public DeviceNotFoundException(String message) {
    super(message);
  }
}
