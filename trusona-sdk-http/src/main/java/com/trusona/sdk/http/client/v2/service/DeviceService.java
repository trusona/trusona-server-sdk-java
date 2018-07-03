package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.resources.dto.Device;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeviceService {
  @GET("/api/v2/devices/{deviceIdentifier}")
  Call<Device> getDevice(@Path("deviceIdentifier") String deviceIdentifier);
}
