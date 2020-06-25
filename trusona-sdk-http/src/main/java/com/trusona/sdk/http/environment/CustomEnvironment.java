package com.trusona.sdk.http.environment;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import okhttp3.logging.HttpLoggingInterceptor;

public class CustomEnvironment implements Environment {

  private final String endpointUrl;

  public CustomEnvironment(String endpointUrl) {
    this.endpointUrl = endpointUrl;
  }

  @Override
  public HttpLoggingInterceptor.Level getLoggingLevel() {
    return HttpLoggingInterceptor.Level.BASIC;
  }

  @Override
  public String getEndpointUrl() {
    return endpointUrl;
  }

  @Override
  public boolean equals(Object object) {
    return object != null && getClass().equals(object.getClass()) && reflectionEquals(this, object);
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(1789, 73837, this);
  }
}
