package com.trusona.sdk.http;

import com.trusona.sdk.resources.exception.TrusonaException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class CallHandler<T> {
  private final Call<T> call;

  public CallHandler(Call<T> call) {
    this.call = call;
  }

  public T handle(ErrorHandler... errorHandlers) throws TrusonaException {
    try {
      Response<T> response = call.execute();

      for (ErrorHandler errorHandler : errorHandlers) {
        errorHandler.handleErrors(response);
      }
      return response.body();
    }
    catch (IOException e) {
      throw new TrusonaException("A network related error occurred trying to get a trucode. " +
        "You should double check that you can connect to Trusona and try your request again.", e);
    }
  }
}
