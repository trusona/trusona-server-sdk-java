package com.trusona.sdk.http;

import retrofit2.Retrofit;

public class EndpointMutator implements RetrofitMutator {
  private final String endpointUrl;

  public EndpointMutator(String endpointUrl) {
    this.endpointUrl = endpointUrl;
  }

  @Override
  public Retrofit mutate(Retrofit.Builder builder) {
    return builder.baseUrl(endpointUrl).build();
  }
}
