package com.trusona.sdk.http.client.interceptor

import com.trusona.sdk.http.ApiCredentials
import com.trusona.sdk.http.client.InterceptorSpec
import com.trusona.sdk.http.client.security.HmacSignatureGenerator
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse

class HmacAuthInterceptorSpec extends InterceptorSpec {
  def mockSigner = Mock(HmacSignatureGenerator)

  HmacAuthInterceptor sut = new HmacAuthInterceptor(mockSigner, new ApiCredentials("token", "secret"))

  def "intercept should apply an Authorization header "() {
    given:
    mockSigner.getSignature(_, "secret") >> "signature"
    mockWebServer.enqueue(new MockResponse())


    when:
    client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    def req = mockWebServer.takeRequest()

    then:
    req.getHeader("Authorization") == "TRUSONA token:signature"

  }

  def "intercept should verify a X-Signature header"() {
    given:
    mockSigner.getSignature(_, "secret") >> "signature"
    mockWebServer.enqueue(new MockResponse()
      .addHeader("X-Signature", "signature")
      .setResponseCode(200))

    when:
    def res = client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    then:
    res.code() == 200
  }

  def "intercept should set status to 401, and body to {} when X-Signature header is not valid"() {
    given:
    mockSigner.getSignature(_, "secret") >> "somethingelse"
    mockWebServer.enqueue(new MockResponse()
      .addHeader("X-Signature", "signature")
      .setResponseCode(200))

    when:
    def res = client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    then:
    res.code() == 401
    res.body().string() == "{}"
  }

  def "intercept should set status to 401 and body to {} when X-Signature header is not present"() {
    given:
    mockSigner.getSignature(_, "secret") >> "somethingelse"
    mockWebServer.enqueue(new MockResponse().setResponseCode(200))

    when:
    def res = client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    then:
    res.code() == 401
    res.body().string() == "{}"
  }

  def "intercept should not validate error responses"() {
    given:
    mockSigner.getSignature(_, "secret") >> "somethingelse"
    mockWebServer.enqueue(new MockResponse().setResponseCode(422))

    when:
    def res = client.newCall(new Request.Builder()
      .url(mockWebServer.url("/"))
      .build()
    ).execute()

    then:
    res.code() == 422
  }
}
