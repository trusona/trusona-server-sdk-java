package com.trusona.sdk.http.client

import com.trusona.sdk.http.Slf4jLogger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import spock.lang.Specification

abstract class InterceptorSpec extends Specification {

  abstract Interceptor getSut()

  OkHttpClient client

  MockWebServer mockWebServer

  def setup() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new Slf4jLogger())
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    client = new OkHttpClient().newBuilder()
      .addInterceptor(getSut())
      .addNetworkInterceptor(loggingInterceptor)
      .build()

    mockWebServer = new MockWebServer()
    mockWebServer.start()
  }

  def cleanup() {
    mockWebServer.shutdown()
  }
}
