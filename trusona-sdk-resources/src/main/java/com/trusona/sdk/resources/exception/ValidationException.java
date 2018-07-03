package com.trusona.sdk.resources.exception;

import java.util.List;
import java.util.Map;

/**
 * A representation of a validation failure when sending requests to Trusona.
 */
public class ValidationException extends TrusonaException {
  private static final long serialVersionUID = -754900679780230285L;

  private Map<String, List<String>> fieldErrors;

  public ValidationException(String message, Map<String, List<String>> fieldErrors) {
    super(message);
    this.fieldErrors = fieldErrors;
  }

  /**
   * Gets the mapping of field names to error messages for each field. Example JSON representation:
   *
   * <pre>
   * { "device_identifier": ["must not be blank"] }
   * </pre>
   *
   * @return the field errors.
   */
  public Map<String, List<String>> getFieldErrors() {
    return fieldErrors;
  }
}
