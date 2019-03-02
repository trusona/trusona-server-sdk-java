package com.trusona.sdk

import com.trusona.sdk.config.JacksonConfig
import com.trusona.sdk.http.ApiCredentials
import com.trusona.sdk.http.ServiceGenerator
import com.trusona.sdk.http.client.security.HmacMessage
import com.trusona.sdk.http.client.security.HmacSignatureGenerator
import com.trusona.sdk.http.environment.Environment
import com.trusona.sdk.http.environment.ProdEnvironment
import com.trusona.sdk.resources.DevicesApi
import com.trusona.sdk.resources.TrusonaficationApi
import com.trusona.sdk.resources.UsersApi
import com.trusona.sdk.resources.dto.Trusonafication
import com.trusona.sdk.resources.dto.UserDevice
import com.trusona.sdk.resources.dto.VerificationStatus
import com.trusona.sdk.resources.dto.WebSdkConfig
import com.trusona.sdk.resources.exception.DeviceAlreadyBoundException
import com.trusona.sdk.resources.exception.DeviceNotFoundException
import com.trusona.sdk.resources.exception.TrusonaException
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

import static JacksonConfig.dateFormat

class TrusonaSpec extends Specification {

  MockWebServer mockWebServer

  Environment mockEnvironment
  ApiCredentials mockCredentials
  HmacSignatureGenerator mockHmacGenerator
  ServiceGenerator mockServiceGenerator

  TrusonaficationApi mockTrusonaficationClient
  DevicesApi mockDevicesClient

  Trusona sut

  def getBodyAs(Buffer buffer, Class clazz) {
    return JacksonConfig.objectMapper.readValue(buffer.inputStream(), clazz)
  }

  def setup() {
    mockWebServer = new MockWebServer()
    mockWebServer.start()

    def token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0cnVhZG1pbi5hcGkudHJ1c29uYS5jb20iLCJzdWIiOiIwZjAzNDhmMC00NmQ2LTQ3YzktYmE0ZC0yZTdjZDdmODJlM2UiLCJhdWQiOiJhcGkudHJ1c29uYS5jb20iLCJleHAiOjE1MTk4ODU0OTgsImlhdCI6MTQ4ODMyNzg5OCwianRpIjoiNzg4YWYwNzAtNDBiOS00N2MxLWE3ZmUtOGUwZmE1NWUwMDE1IiwiYXRoIjoiUk9MRV9UUlVTVEVEX1JQX0NMSUVOVCJ9.2FNvjG9yB5DFEcNijk8TryRtKVffiDARRcRIb75Z_Pp85MxW63rhzdLFIN6PtQ1Tzb8lHPPM_4YOe-feeLOzWw"
    mockCredentials = new ApiCredentials(token, "secret")
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

    mockTrusonaficationClient = Mock(TrusonaficationApi)
    mockDevicesClient = Mock(DevicesApi)

    sut = new Trusona(mockServiceGenerator, mockCredentials, mockTrusonaficationClient, mockDevicesClient, Mock(UsersApi))
    sut.pollingInterval = Duration.ofMillis(1)
  }

  def cleanup() {
    mockWebServer.shutdown()
  }

  def "createUserDevice should return a UserDeviceResponse when API returns success"() {
    given:
    def id = UUID.randomUUID()
    def body = """
      {
        "id": "${id.toString()}",
        "user_identifier": "user",
        "device_identifier": "device",
        "active": false
      }""".stripIndent()

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(201)
    )

    when:
    def res = sut.createUserDevice("user", "device")
    def response = mockWebServer.takeRequest()

    then:
    !res.active
    res.activationCode == id.toString()
    res.deviceIdentifier == "device"
    res.userIdentifier == "user"

    response.method == "POST"
    response.path == "/api/v2/user_devices"
    getBodyAs(response.body, UserDevice) == new UserDevice(userIdentifier: "user", deviceIdentifier: "device")
  }

  def "createUserDevice should return a DeviceAlreadyBoundException when it gets a 409"() {
    given:
    def body = """
      {
        "error": "Something bad occurred",
        "message": "This is an error message",
        "description": "Have a longer description too"
      }""".stripIndent()

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(409)
    )

    when:
    sut.createUserDevice("user", "device")

    then:
    def error = thrown(DeviceAlreadyBoundException)
    error.message == "A different user has already been bound to this device."
  }

  def "createUserDevice should return a DeviceNotFoundException when it gets a 424"() {
    given:
    def body = """
      {
        "error": "Something bad occurred",
        "message": "This is an error message",
        "description": "Have a longer description too"
      }""".stripIndent()

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(424)
    )

    when:
    sut.createUserDevice("user", "device")

    then:
    def error = thrown(DeviceNotFoundException)
    error.message == "The device you are attempting to bind to a user does not exist. The device will need to be re-registered with Trusona before attempting to bind it again."
  }

  def "createUserDevice should handle generic exceptions"() {
    given:
    mockWebServer.enqueue(new MockResponse().setResponseCode(500))

    when:
    sut.createUserDevice("user", "device")

    then:
    thrown(TrusonaException)
  }

  def "activateUserDevice should return true when a UserDevice is activated"() {
    given:
    def id = UUID.randomUUID()
    def activationCode = UUID.randomUUID().toString()
    def body = """
      {
        "id": "${id.toString()}",
        "user_identifier": "user",
        "device_identifier": "device",
        "active": true
      }""".stripIndent()

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(200)
    )

    when:
    def res = sut.activateUserDevice(activationCode)
    def request = mockWebServer.takeRequest()

    then:
    res
    request.method == "PATCH"
    request.path == "/api/v2/user_devices/" + activationCode
    getBodyAs(request.body, UserDevice) == new UserDevice(active: true)
  }

  def "activateUserDevice should throw a DeviceNotFoundException when it gets a 404"() {
    given:
    def body = """
      {
        "error": "Something bad occurred",
        "message": "This is an error message",
        "description": "Have a longer description too"
      }""".stripIndent()

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(404)
    )

    when:
    sut.activateUserDevice("activationCode")

    then:
    def error = thrown(DeviceNotFoundException)
    error.message == "The device you are attempting to activate does not exist. You will need to re-register the device and re-bind it to the user to get a new activation code."
  }

  def "activateUserDevice should handle generic exceptions"() {
    given:
    mockWebServer.enqueue(new MockResponse().setResponseCode(500))

    when:
    sut.activateUserDevice("code")

    then:
    thrown(TrusonaException)
  }

  def "createTrusonafication should delegate to TrusonaficationClient"() {
    given:
    def truso = Trusonafication.essential()
      .truCode(UUID.randomUUID())
      .action('eat')
      .resource('your lunch')
      .build()

    when:
    sut.createTrusonafication(truso)

    then:
    1 * mockTrusonaficationClient.createTrusonafication(truso)
  }

  def "getTrusonaficationResult should delegate to TrusonaficationClient"() {
    given:
    def trusonaficationId = UUID.randomUUID()

    when:
    sut.getTrusonaficationResult(trusonaficationId)

    then:
    1 * mockTrusonaficationClient.getTrusonaficationResult(trusonaficationId)
  }

  def "getWebSdkConfig should return the Web SDK config as JSON"() {
    when:

    def res = sut.getWebSdkConfig()

    then:
    res == JacksonConfig.objectMapper.writeValueAsString(new WebSdkConfig(mockEnvironment.endpointUrl, mockCredentials.parsedToken.subject))
  }

  def "getPairedTrucode should use the main endpoint if no configured endpoints exist"() {
    given:
    def truCodeId = UUID.randomUUID()
    mockWebServer.enqueue(new MockResponse()
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(200)
      .setBody("""\
        {
          "id": "${truCodeId.toString()}",
          "paired": true,
          "identifier": "foobar"
        }
      """)
    )

    when:
    def res = sut.getPairedTruCode(truCodeId)

    then:
    res.identifier == "foobar"
    res.id == truCodeId
  }

  def "getPairedTrucode should return null if no TruCodes are found"() {
    given:
    def truCodeId = UUID.randomUUID()
    mockWebServer.enqueue(new MockResponse()
      .setHeader('X-Signature', 'signature')
      .setResponseCode(404))

    when:
    def res = sut.getPairedTruCode(truCodeId)

    then:
    res == null
  }

  def "getPairedTruCode with a specified timeout should poll until a paired TruCode is found (or Timeout is reached)"() {
    given:
    sut.pollingInterval = Duration.ofSeconds(5)

    mockWebServer.enqueue(new MockResponse()
      .setHeader('X-Signature', 'signature')
      .setResponseCode(404))

    mockWebServer.enqueue(new MockResponse()
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(200)
      .setBody("""\
        {
          "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
          "identifier": "wall-e@dogs.example.net"
        }
        """)
    )

    when:
    def res = sut.getPairedTruCode(UUID.fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"), 500)

    then:
    res.identifier == "wall-e@dogs.example.net"
  }

  def "getIdentityDocument should return an identity document when one exists"() {
    given:
    def documentBody = '''
    {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9 ",
      "hash": "foobar",
      "verified_at": "2018-01-23T23:28:45Z",
      "verification_status": "UNVERIFIED",
      "type": "AAMVA_DRIVERS_LICENSE"
    }
    '''

    mockWebServer.enqueue(new MockResponse()
      .setBody(documentBody)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
      .setResponseCode(200)
    )

    when:
    def res = sut.getIdentityDocument(UUID.fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"))

    then:
    res.id.toString() == "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"
    res.hash == "foobar"
    res.verificationStatus == VerificationStatus.UNVERIFIED
    res.type == "AAMVA_DRIVERS_LICENSE"
    res.verifiedAt == dateFormat.parse("2018-01-23T23:28:45Z")
  }

  def "getIdentityDocument should return null on 404"() {
    given:
    mockWebServer.enqueue(new MockResponse()
      .setHeader('X-Signature', 'signature')
      .setResponseCode(404))

    when:
    def res = sut.getIdentityDocument(UUID.fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"))

    then:
    res == null
  }

  @Unroll
  def "getIdentityDocument should throw a TrusonaException when status is #status.inspect()"() {
    given:
    mockWebServer.enqueue(new MockResponse()
      .setHeader('X-Signature', 'signature')
      .setResponseCode(status))

    when:
    def res = sut.getIdentityDocument(UUID.fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"))

    then:
    thrown(TrusonaException)

    where:
    status << [403, 500]
  }

  def "findIdentityDocuments should return an empty list when there are no documents"() {
    given:
    mockWebServer.enqueue(new MockResponse()
      .setBody("[]")
      .setHeader("ContentType", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
    )

    when:
    def res = sut.findIdentityDocuments("foobar")

    then:
    res.size() == 0
  }

  def "findIdentityDocuments should return a list of identity documents when there are documents"() {
    given:
    def body = """
    [
      {
        "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8",
        "hash": "foobar",
        "verification_status": "UNVERIFIED",
        "verified_at": "2018-01-23T23:28:45Z",
        "type": "AAMVA_DRIVERS_LICENSE"
      }
    ]
    """

    mockWebServer.enqueue(new MockResponse()
      .setBody(body)
      .setHeader("Content-Type", "application/json; charset=utf-8")
      .setHeader('X-Signature', 'signature')
    )

    when:
    def res = sut.findIdentityDocuments("foobar")

    then:
    res.size() == 1
  }

  @Unroll
  def "findIdentityDocuments should throw a TrusonaException when status is #status.inspect()"() {
    given:
    mockWebServer.enqueue(new MockResponse()
      .setHeader('X-Signature', 'signature')
      .setResponseCode(status))

    when:
    def res = sut.findIdentityDocuments("foobar")

    then:
    thrown(TrusonaException)

    where:
    status << [400, 403, 500]
  }

  def "getDevice should delegate to the DevicesApi"() {
    given:
    def deviceIdentifier = 'wall-e'

    when:
    sut.getDevice(deviceIdentifier)

    then:
    1 * mockDevicesClient.getDevice(deviceIdentifier)
    0 * _
  }

  def "deactivateUser should delegate to the UsersApi"() {
    when:
    sut.deactivateUser("any-identifier")

    then:
    1 * _.deactivateUser("any-identifier")
    0 * _
  }
}
