package com.trusona.sdk.http.environment;

import java.util.Objects;

import okhttp3.logging.HttpLoggingInterceptor;

public class EuProdEnvironment implements Environment {

  private static final String ENDPOINT_URL = "https://api.eu.trusona.net";

  @Override
  public HttpLoggingInterceptor.Level getLoggingLevel() {
    return HttpLoggingInterceptor.Level.NONE;
  }

  @Override
  public String getEndpointUrl() {
    return ENDPOINT_URL;
  }

  @Override
  public boolean equals(Object object) {
    return object != null && getClass().equals(object.getClass());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getEndpointUrl());
  }
}
