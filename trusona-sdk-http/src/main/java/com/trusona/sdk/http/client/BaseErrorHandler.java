package com.trusona.sdk.http.client;

import com.trusona.sdk.config.JacksonConfig;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.resources.exception.TrusonaException;
import retrofit2.Response;

import java.io.IOException;
import java.io.Reader;

public abstract class BaseErrorHandler implements ErrorHandler {

  protected ErrorResponse getErrorResponse(Response response) throws TrusonaException {
    Reader errorBody = response.errorBody().charStream();

    try {
      return JacksonConfig.getObjectMapper().readValue(errorBody, ErrorResponse.class);
    }
    catch (IOException e) {
      throw new TrusonaException("An unknown error occurred. Contact Trusona to determine the exact cause.", e);
    }
    finally {
      try {
        errorBody.close();
      }
      catch (IOException e) {
      }
    }
  }
}
