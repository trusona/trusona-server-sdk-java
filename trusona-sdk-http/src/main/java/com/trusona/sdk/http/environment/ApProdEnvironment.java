package com.trusona.sdk.http.environment;

import okhttp3.logging.HttpLoggingInterceptor;

@Deprecated
public class ApProdEnvironment implements Environment {
  private static final String ENDPOINT_URL = "https://api.ap.trusona.net";
  @Override
  public HttpLoggingInterceptor.Level getLoggingLevel() {
    return HttpLoggingInterceptor.Level.NONE;
  }

  @Override
  public String getEndpointUrl() {
    return ENDPOINT_URL;
  }
}