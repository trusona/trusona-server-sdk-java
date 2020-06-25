package com.trusona.sdk.http.environment;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import okhttp3.logging.HttpLoggingInterceptor;

public class EuUatEnvironment implements Environment {

  private static final String ENDPOINT_URL = "https://api.staging.eu.trusona.net";

  @Override
  public HttpLoggingInterceptor.Level getLoggingLevel() {
    return HttpLoggingInterceptor.Level.BASIC;
  }

  @Override
  public String getEndpointUrl() {
    return ENDPOINT_URL;
  }

  @Override
  public boolean equals(Object object) {
    return object != null && getClass().equals(object.getClass()) &&
        Objects.equals(getEndpointUrl(), ((EuUatEnvironment) object).getEndpointUrl());
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(1787, 73839, this);
  }
}
