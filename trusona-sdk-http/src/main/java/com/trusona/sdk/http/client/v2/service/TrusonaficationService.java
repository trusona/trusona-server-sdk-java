package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.http.client.v2.response.TrusonaficationResponse;
import com.trusona.sdk.resources.dto.Trusonafication;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.UUID;

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
public interface TrusonaficationService {
  @POST("/api/v2/trusonafications")
  Call<TrusonaficationResponse> createTrusonafication(@Body Trusonafication request);

  @GET("/api/v2/trusonafications/{id}")
  Call<TrusonaficationResponse> getTrusonafication(@Path("id") UUID id);
}
