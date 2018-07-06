package com.trusona.sdk.http.environment;

import okhttp3.logging.HttpLoggingInterceptor;

public interface Environment {
  HttpLoggingInterceptor.Level getLoggingLevel();

  String getEndpointUrl();
}
