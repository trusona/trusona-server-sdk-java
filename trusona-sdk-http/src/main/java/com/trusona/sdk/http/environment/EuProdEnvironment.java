package com.trusona.sdk.http.environment;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    return object != null && getClass().equals(object.getClass()) &&
        Objects.equals(getEndpointUrl(), ((EuProdEnvironment) object).getEndpointUrl());
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(1189, 71837, this);
  }
}
