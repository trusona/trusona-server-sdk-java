package com.trusona.sdk.http.client;

import com.trusona.sdk.config.JacksonConfig;
import com.trusona.sdk.http.ErrorHandler;
import com.trusona.sdk.http.client.v2.response.ErrorResponse;
import com.trusona.sdk.resources.exception.TrusonaException;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseErrorHandler implements ErrorHandler {

  protected ErrorResponse getErrorResponse(Response<?> response) throws TrusonaException {
    Optional<ResponseBody> errorBody = Optional.ofNullable(response.errorBody());

    if (errorBody.isPresent()) {
      try {
        return JacksonConfig.getObjectMapper().readValue(errorBody.get().charStream(), ErrorResponse.class);
      }
      catch (IOException e) {
        throw new TrusonaException("Failed to parse error response", e);
      }
      finally {
        errorBody.get().close();
      }
    }
    else {
      return new ErrorResponse("MISSING_ERROR", "Response error did not have a body. Contact Trusona to determine the exact cause");
    }
  }
}