package com.trusona.sdk.http.environment;

import okhttp3.logging.HttpLoggingInterceptor;

@Deprecated
public class ApUatEnvironment implements Environment {
  private static final String ENDPOINT_URL = "https://api.staging.ap.trusona.net";
  @Override
  public HttpLoggingInterceptor.Level getLoggingLevel() {
    return HttpLoggingInterceptor.Level.BASIC;
  }

  @Override
  public String getEndpointUrl() {
    return ENDPOINT_URL;
  }
}
