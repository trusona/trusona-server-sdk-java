package com.trusona.sdk.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.trusona.sdk.config.JacksonConfig
import com.trusona.sdk.http.client.security.HmacMessage
import com.trusona.sdk.http.client.security.HmacSignatureGenerator
import com.trusona.sdk.http.environment.Environment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import spock.lang.Specification

class ServiceGeneratorSpec extends Specification {

  OkHttpClient client

  MockWebServer mockWebServer

  Retrofit retrofit

  Environment mockEnvironment

  HmacSignatureGenerator mockSignatureGenerator

  ApiCredentials credentials = new ApiCredentials("token", "secret")

  def setup() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new Slf4jLogger())
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    client = new OkHttpClient().newBuilder()
      .addNetworkInterceptor(loggingInterceptor)
      .build()

    mockWebServer = new MockWebServer()
    mockWebServer.start()

    retrofit = new Retrofit.Builder()
      .client(client)
      .baseUrl(mockWebServer.url("/"))
      .addConverterFactory(JacksonConverterFactory.create(JacksonConfig.objectMapper))
      .build()

    mockEnvironment = new Environment() {
      @Override
      HttpLoggingInterceptor.Level getLoggingLevel() {
        return HttpLoggingInterceptor.Level.BASIC;
      }

      @Override
      String getEndpointUrl() {
        return mockWebServer.url("/").toString()
      }
    }

    mockSignatureGenerator = new HmacSignatureGenerator() {
      @Override
      String getSignature(HmacMessage message, String secret) throws IOException {
        return "signature"
      }
    }
  }


  def cleanup() {
    mockWebServer.shutdown()
  }

  static class TestObject {
    private String foo

    public String getFoo() {
      return foo
    }

    public void setFoo(String foo) {
      this.foo = foo
    }
  }

  interface TestService {
    @GET("/test")
    Call<TestObject> testGet()

    @POST("/test")
    Call<TestObject> testPost(@Body TestObject object)
  }

  def "getService should create a service"() {
    given:
    def sut = new ServiceGenerator(retrofit)

    when:
    def call = sut.getService(TestService).testGet()

    then:
    call.request().url() == mockWebServer.url("/test")
  }

  def "getService should let you mutate retrofit"() {
    given:
    def sut = new ServiceGenerator(retrofit)
    def mutator = new RetrofitMutator() {
      @Override
      Retrofit mutate(Retrofit.Builder builder) {
        return builder
          .baseUrl("http://example.com/")
          .build()
      }
    }

    when:
    def call = sut.getService(TestService, mutator).testGet()

    then:
    call.request().url().host() == "example.com"
  }

  def "create should use the environment endpoint url"() {
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    when:
    def call = sut.getService(TestService).testGet()

    then:
    call.request().url().host() == mockWebServer.url("/").host()
    call.request().url().scheme() == mockWebServer.url("/").scheme()
  }

  def "create should create a client that can deserialize JSON"(){
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    mockWebServer.enqueue(new MockResponse()
      .setBody('{"foo":"bar"}')
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader("X-Signature", "signature")
      .setResponseCode(200)
    )

    when:
    def res = sut.getService(TestService).testGet().execute()

    then:
    res.body().foo == "bar"
  }

  def "create should create a client that adds an X-Date header"(){
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    mockWebServer.enqueue(new MockResponse()
      .setBody('{"foo":"bar"}')
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader("X-Signature", "signature")
      .setResponseCode(200)
    )

    when:
    sut.getService(TestService).testGet().execute()
    def req = mockWebServer.takeRequest()

    then:
    !req.getHeader("X-Date").empty
  }

  def "create should create a client that adds the Trusona user agent"(){
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    mockWebServer.enqueue(new MockResponse()
      .setBody('{"foo":"bar"}')
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader("X-Signature", "signature")
      .setResponseCode(200)
    )

    when:
    sut.getService(TestService).testGet().execute()
    def req = mockWebServer.takeRequest()

    then:
    req.getHeader("User-Agent") == "TrusonaServerSdk/1.0"
  }

  def "create should create a client that HMACs requests"() {
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    mockWebServer.enqueue(new MockResponse()
      .setBody('{"foo":"bar"}')
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader("X-Signature", "signature")
      .setResponseCode(200)
    )

    when:
    sut.getService(TestService).testPost(new TestObject(foo: "bar")).execute()
    def req = mockWebServer.takeRequest()
    def authorization = req.getHeader("Authorization")

    then:
    !authorization.empty
    authorization == "TRUSONA ${credentials.token}:${mockSignatureGenerator.getSignature(new RecordedRequestHmacMessage(req), credentials.secret)}"
  }

  def "create should use the JacksonConfig object mapper from the http project"() {
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    when:
    def res = sut.retrofit.converterFactories().find {
      it instanceof JacksonConverterFactory && !it.mapper.getDeserializationConfig().hasDeserializationFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.mask)
    }

    then:
    res
  }

  def "getBaseUrl should return the configured base url"() {
    given:
    def sut = ServiceGenerator.create(mockEnvironment, credentials, mockSignatureGenerator)

    when:
    def res = sut.getBaseUrl()

    then:
    res == mockWebServer.url("/").toString()
  }
}
