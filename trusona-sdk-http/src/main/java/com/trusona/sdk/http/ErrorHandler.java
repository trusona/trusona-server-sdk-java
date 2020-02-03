package com.trusona.sdk.http;

import com.trusona.sdk.resources.exception.TrusonaException;
import retrofit2.Response;

public interface ErrorHandler {
  void handleErrors(Response<?> response) throws TrusonaException;
}