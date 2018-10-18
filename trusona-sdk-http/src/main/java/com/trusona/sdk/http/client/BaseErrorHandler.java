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

  protected ErrorResponse getErrorResponse(Response response) throws TrusonaException {
    Optional<ResponseBody> errorBody = Optional.ofNullable(response.errorBody());

    if (errorBody.isPresent()) {
      try {
        return JacksonConfig.getObjectMapper().readValue(errorBody.get().charStream(), ErrorResponse.class);
      }
      catch (IOException e) {
        throw new TrusonaException("An unknown error occurred. Contact Trusona to determine the exact cause.", e);
      }
      finally {
        errorBody.get().close();
      }
    }
    else {
      return new ErrorResponse("UNKNOWN", "An unknown error occurred. Contact Trusona to determine the exact cause");
    }
  }
}