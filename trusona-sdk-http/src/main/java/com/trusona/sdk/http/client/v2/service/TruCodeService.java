package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.resources.dto.TruCode;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TruCodeService {
  @GET("/api/v2/paired_trucodes/{id}")
  Call<TruCode> getPairedTrucode(@Path("id") UUID truCodeId);
}
