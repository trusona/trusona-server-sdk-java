package com.trusona.sdk.http.client.v2.response;

import com.trusona.sdk.http.client.v2.BaseRequestResponse;

import java.util.List;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class DiscoverableConfigResponse extends BaseRequestResponse {
  private static final long serialVersionUID = 7347804786051L;

  private List<String> endpoints;

  public List<String> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<String> endpoints) {
    this.endpoints = endpoints;
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(3457, 33221, this);
  }
}
