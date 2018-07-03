package com.trusona.sdk.http.client.interceptor

import com.trusona.sdk.http.client.InterceptorSpec
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse

class TrusonaHeaderInterceptorSpec extends InterceptorSpec {

  Interceptor sut = new TrusonaHeaderInterceptor()

  def "TrusonaHeaderInterceptor should add an X-Date header"() {
    given:
    mockWebServer.enqueue(new MockResponse())

    when:
    client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    def res = mockWebServer.takeRequest()

    then:
    !res.getHeader("X-Date").empty
  }

  def "TrusonaHeaderInterceptor should add a User-Agent header"() {
    given:
    mockWebServer.enqueue(new MockResponse())

    when:
    client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    def res = mockWebServer.takeRequest()

    then:
    res.getHeader("User-Agent") == "TrusonaServerSdk/1.0"

  }
}
