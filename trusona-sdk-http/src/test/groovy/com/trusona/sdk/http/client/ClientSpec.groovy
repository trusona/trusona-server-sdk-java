package com.trusona.sdk.http.client

import com.trusona.sdk.config.JacksonConfig
import com.trusona.sdk.http.ApiCredentials
import com.trusona.sdk.http.ServiceGenerator
import com.trusona.sdk.http.client.security.HmacMessage
import com.trusona.sdk.http.client.security.HmacSignatureGenerator
import com.trusona.sdk.http.environment.Environment
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import spock.lang.Specification


class ClientSpec extends Specification {
  MockWebServer mockWebServer

  Environment mockEnvironment
  ApiCredentials mockCredentials
  HmacSignatureGenerator mockHmacGenerator
  ServiceGenerator mockServiceGenerator

  def getBodyAs(Buffer buffer, Class clazz) {
    return JacksonConfig.objectMapper.readValue(buffer.inputStream(), clazz)
  }

  def signedResponse(int status, String body) {
    return new MockResponse()
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(status)
      .setBody(body)
  }

  def setup() {
    mockWebServer = new MockWebServer()
    mockWebServer.start()

    mockCredentials = new ApiCredentials("token", "secret")
    mockEnvironment = new Environment() {
      @Override
      HttpLoggingInterceptor.Level getLoggingLevel() {
        return HttpLoggingInterceptor.Level.BODY;
      }

      @Override
      String getEndpointUrl() {
        return mockWebServer.url("/").toString()
      }
    }

    mockHmacGenerator = new HmacSignatureGenerator() {
      @Override
      String getSignature(HmacMessage message, String secret) throws IOException {
        return "signature"
      }
    }

    mockServiceGenerator = ServiceGenerator.create(mockEnvironment, mockCredentials, mockHmacGenerator)
  }

  def cleanup() {
    mockWebServer.shutdown()
  }
}
