package com.trusona.sdk.http;

import com.trusona.sdk.http.client.BaseErrorHandler;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.resources.exception.InvalidCredentialsException;
import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.ValidationException;
import retrofit2.Response;


public class GenericErrorHandler extends BaseErrorHandler implements ErrorHandler {

  @Override
  public void handleErrors(Response<?> response) throws TrusonaException {
    if (response.isSuccessful()) {
      return;
    }

    if (response.code() == 404) {
      //no-op since 404 results in null
      return;
    }

    if (response.code() == 400) {
      throw new TrusonaException(
        "The Trusona SDK was unable to fulfill your request do to an error with the SDK. Contact Trusona to determine the issue.");
    }

    if (response.code() == 403) {
      throw new InvalidCredentialsException();
    }

    if (response.code() >= 500 && response.code() < 600) {
      throw new TrusonaException(
        "The server was unable to process your request at this time. Feel free to try your request again later.");
    }

    ErrorResponse errorResponse = getErrorResponse(response);

    if (errorResponse.getFieldErrors() == null || errorResponse.getFieldErrors().isEmpty()) {
      throw new TrusonaException(errorResponse.getDescription());
    }

    throw new ValidationException(errorResponse.getDescription(), errorResponse.getFieldErrors());
  }
}