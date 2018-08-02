package com.trusona.sdk.http.client.v2.service;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface UserService {

  @DELETE("/api/v2/users/{userIdentifier}")
  Call<Void> deleteUser(@Path("userIdentifier") String userIdentifier);

}