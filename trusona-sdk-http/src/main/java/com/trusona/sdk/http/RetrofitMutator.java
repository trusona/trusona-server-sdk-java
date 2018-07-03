package com.trusona.sdk.http;

import retrofit2.Retrofit;

public interface RetrofitMutator {
  Retrofit mutate(Retrofit.Builder builder);
}
