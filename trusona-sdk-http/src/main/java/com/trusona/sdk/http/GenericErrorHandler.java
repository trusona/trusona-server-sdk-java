package com.trusona.sdk.http;

import com.trusona.sdk.http.client.BaseErrorHandler;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.resources.exception.TrusonaException;
import com.trusona.sdk.resources.exception.ValidationException;
import retrofit2.Response;


public class GenericErrorHandler extends BaseErrorHandler implements ErrorHandler {
  @Override
  public void handleErrors(Response response) throws TrusonaException {

    if (response.code() == 400) {
      throw new TrusonaException(
        "The Trusona SDK was unable to fulfill your request do to an error with the SDK. Contact Trusona to determine the issue.");
    }
    else if (response.code() == 403) {
      throw new TrusonaException(
        "The token and/or secret you are using are invalid. Contact Trusona to get valid Server SDK credentials.");
    }
    else if (response.code() == 404) {
      //no-op since 404 results in null
    }
    else if (response.code() >= 500 && response.code() < 600) {
      throw new TrusonaException(
        "The server was unable to process your request at this time. Feel free to try your request again later.");
    }
    else if (!response.isSuccessful()) {
      ErrorResponse errorResponse = getErrorResponse(response);

      if (errorResponse.getFieldErrors() != null && !errorResponse.getFieldErrors().isEmpty()) {
        throw new ValidationException(errorResponse.getDescription(), errorResponse.getFieldErrors());
      }
      else {
        throw new TrusonaException(errorResponse.getDescription());
      }
    }
  }
}
