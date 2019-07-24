package com.trusona.sdk.http.client.v2.response;

import com.trusona.sdk.http.client.v2.BaseResponse;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Map;

public class ErrorResponse extends BaseResponse {
  private static final long serialVersionUID = -207143182156053353L;

  private String error;
  private String message;
  private String description;
  private Map<String, List<String>> fieldErrors;

  public ErrorResponse() {

  }

  public ErrorResponse(String error, String message, String description, Map<String, List<String>> fieldErrors) {
    this(error, message, description);
    this.fieldErrors = fieldErrors;
  }

  public ErrorResponse(String error, String message) {
    this(error, message, null);
  }

  public ErrorResponse(String error, String message, String description) {
    this.error = error;
    this.message = message;
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, List<String>> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(Map<String, List<String>> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(2311, 3431, this);
  }
}
