package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.http.client.v2.response.DiscoverableConfigResponse;
import com.trusona.sdk.resources.dto.TruCode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.UUID;

public interface TruCodeService {
  @GET("/trucode/v1/config")
  Call<DiscoverableConfigResponse> getDiscoverableConfig();

  @GET("/api/v2/paired_trucodes/{id}")
  Call<TruCode> getPairedTrucode(@Path("id") UUID truCodeId);
}
