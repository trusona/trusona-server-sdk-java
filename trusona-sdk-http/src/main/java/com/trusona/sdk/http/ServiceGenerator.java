package com.trusona.sdk.http;

import com.trusona.sdk.config.JacksonConfig;
import com.trusona.sdk.http.client.interceptor.HmacAuthInterceptor;
import com.trusona.sdk.http.client.interceptor.TrusonaHeaderInterceptor;
import com.trusona.sdk.http.client.security.HmacSignatureGenerator;
import com.trusona.sdk.http.environment.Environment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceGenerator {
  private Retrofit retrofit;

  private ServiceGenerator(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  public <T> T getService(Class<T> serviceClass) {
    return retrofit.create(serviceClass);
  }

  public <T> T getService(Class<T> serviceClass, RetrofitMutator mutator) {
    return mutator.mutate(retrofit.newBuilder()).create(serviceClass);
  }

  public String getBaseUrl() {
    return retrofit.baseUrl().toString();
  }

  public static ServiceGenerator create(Environment environment, ApiCredentials apiCredentials, HmacSignatureGenerator hmacSignatureGenerator) {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new Slf4jLogger());
    loggingInterceptor.setLevel(environment.getLoggingLevel());

    OkHttpClient client = new OkHttpClient.Builder()
      .addInterceptor(new TrusonaHeaderInterceptor())
      .addNetworkInterceptor(new HmacAuthInterceptor(hmacSignatureGenerator, apiCredentials))
      .addNetworkInterceptor(loggingInterceptor)
      .build();

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(environment.getEndpointUrl())
      .client(client)
      .addConverterFactory(JacksonConverterFactory.create(JacksonConfig.getObjectMapper()))
      .build();

    return new ServiceGenerator(retrofit);
  }
}
