package com.trusona.sdk.http.client.v2.service;

import com.trusona.sdk.resources.dto.UserDevice;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserDeviceService {
  @POST("/api/v2/user_devices")
  Call<UserDevice> createUserDevice(@Body UserDevice request);

  @PATCH("/api/v2/user_devices/{id}")
  Call<UserDevice> updateUserDevice(@Path("id") String id, @Body UserDevice request);
}

