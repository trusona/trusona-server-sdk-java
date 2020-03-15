package com.trusona.sdk.http.client

import com.trusona.sdk.http.GenericErrorHandler
import com.trusona.sdk.http.client.v2.response.TrusonaficationResponse
import com.trusona.sdk.http.client.v2.response.TrusonaficationResultResponse
import com.trusona.sdk.resources.dto.Trusonafication
import com.trusona.sdk.resources.exception.NoIdentityDocumentsException
import com.trusona.sdk.resources.exception.TrusonaException
import groovy.json.JsonSlurper
import okhttp3.mockwebserver.MockResponse
import spock.lang.Unroll

import java.time.Duration

import static com.trusona.sdk.config.JacksonConfig.getDateFormat
import static com.trusona.sdk.resources.dto.AuthenticatorType.MOBILE_APP
import static com.trusona.sdk.resources.dto.TrusonaficationStatus.ACCEPTED
import static com.trusona.sdk.resources.dto.TrusonaficationStatus.IN_PROGRESS
import static java.util.UUID.fromString
import static java.util.UUID.randomUUID

class TrusonaficationClientSpec extends ClientSpec {

  TrusonaficationClient sut

  def setup() {
    sut = new TrusonaficationClient(mockServiceGenerator, new GenericErrorHandler(), Duration.ofMillis(10))
  }

  def "createTrusonafication should return a result when successful"() {
    given:
    def responseJson = """\
    {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
      "status": "IN_PROGRESS"
    }
    """

    mockWebServer.enqueue(signedResponse(201, responseJson))

    when:
    def res = sut.createTrusonafication(Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('eat')
      .resource('your lunch')
      .build())
    def request = mockWebServer.takeRequest()

    then:
    request.method == 'POST'
    request.path == '/api/v2/trusonafications'
    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == IN_PROGRESS
  }

  def "createTrusonafication should send the trucode id to the trusona service."() {
    given:
    def responseJson = """\
    {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
      "status": "IN_PROGRESS"
    }
    """

    def truCodeId = randomUUID()

    mockWebServer.enqueue(signedResponse(201, responseJson))

    when:
    def res = sut.createTrusonafication(Trusonafication.essential()
      .truCode(truCodeId)
      .action('eat')
      .resource('your lunch')
      .build())

    def request = mockWebServer.takeRequest()
    def map = new JsonSlurper().parse(request.body.readByteArray()) as Map

    then:
    request.method == 'POST'
    request.path == '/api/v2/trusonafications'
    map.trucode_id == truCodeId as String

    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == IN_PROGRESS
  }

  def "createTrusonafication should send the email-address to the trusona service."() {
    given:
    def responseJson = """\
    {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
      "status": "IN_PROGRESS"
    }
    """

    def email = "african-tiger@taco.jones"

    mockWebServer.enqueue(signedResponse(201, responseJson))

    when:
    def res = sut.createTrusonafication(Trusonafication.essential()
      .email(email)
      .action('eat')
      .resource('your lunch')
      .build())

    def request = mockWebServer.takeRequest()
    def map = new JsonSlurper().parse(request.body.readByteArray()) as Map

    then:
    request.method == 'POST'
    request.path == '/api/v2/trusonafications'
    map.email == email

    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == IN_PROGRESS
  }

  def "createTrusonafication using a trusonaId should send the trusonaId to the trusona service."() {
    given:
    def responseJson = """\
    {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
      "status": "IN_PROGRESS"
    }
    """

    def trusonaId = "123456789"

    mockWebServer.enqueue(signedResponse(201, responseJson))

    when:
    def res = sut.createTrusonafication(Trusonafication.essential()
      .trusonaId(trusonaId)
      .action('eat')
      .resource('your lunch')
      .build())

    def request = mockWebServer.takeRequest()
    def map = new JsonSlurper().parse(request.body.readByteArray()) as Map

    then:
    request.method == 'POST'
    request.path == '/api/v2/trusonafications'
    map.trusona_id == trusonaId

    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == IN_PROGRESS
  }

  @Unroll
  def "createTrusonafication should handle generic exceptions (#statusCode)"() {
    given:
    mockWebServer.enqueue(new MockResponse().setResponseCode(statusCode))

    when:
    sut.createTrusonafication(Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('eat')
      .resource('your lunch')
      .build())

    then:
    thrown(TrusonaException)

    where:
    statusCode << [400, 403, 422, 500]
  }

  def "createTrusonafication should throw a NoIdentityDocumentException when the user has no documents and an executive trusonafication is created."() {
    given:
    mockWebServer.enqueue(signedResponse(
      424,
      """
      {
        "error": "NO_DOCUMENTS",
        "message": "User does not meet the requirements to accept this Trusonafication.",
        "description": "The user must show a document, but does not have any"
      }
      """))

    when:
    sut.createTrusonafication(Trusonafication.executive()
      .deviceIdentifier("clayto")
      .action("eat")
      .resource("socks")
      .build()
    )

    then:
    thrown(NoIdentityDocumentsException)
  }

  def "createTrusonafication should throw a generic exception when the service determines the relying-party cannot trusonafy a specific email address"() {
    given:
    mockWebServer.enqueue(signedResponse(
      422,
      """
      {
        "error": "Failed Trusonafication",
        "message": "Relying Party is not allowed to send a trusonafication to 'bob@taco.com'"
      }
      """))

    when:
    sut.createTrusonafication(Trusonafication.executive()
      .email("bob@taco.com")
      .action("eat")
      .resource("socks")
      .build()
    )

    then:
    thrown(TrusonaException)
  }

  def "getTrusonaficationResult should poll while it is IN_PROGRESS"() {
    given:
    def trusonaficationId = randomUUID()
    def inProgressBody = """\
        {
          "id": "$trusonaficationId",
          "status": "IN_PROGRESS"
        }"""
    def acceptedBody = """\
        {
          "id": "$trusonaficationId",
          "status": "ACCEPTED"
        }"""

    mockWebServer.enqueue(signedResponse(201, inProgressBody));

    mockWebServer.enqueue(signedResponse(200, inProgressBody));

    mockWebServer.enqueue(signedResponse(200, acceptedBody))

    when:
    def res = sut.getTrusonaficationResult(trusonaficationId)
    def createRequest = mockWebServer.takeRequest()
    def firstGetRequest = mockWebServer.takeRequest()
    def secondGetRequest = mockWebServer.takeRequest()

    then:
    createRequest.method == 'GET'
    createRequest.path == "/api/v2/trusonafications/$trusonaficationId"

    firstGetRequest.method == 'GET'
    firstGetRequest.path == "/api/v2/trusonafications/$trusonaficationId"

    secondGetRequest.method == 'GET'
    secondGetRequest.path == "/api/v2/trusonafications/$trusonaficationId"

    res.trusonaficationId == trusonaficationId
    res.status == ACCEPTED
  }

  def "getTrusonaficationResult should return null if trusonafication is not found."() {
    given:
    mockWebServer.enqueue(new MockResponse()
      .setResponseCode(404)
    )

    when:
    def res = sut.getTrusonaficationResult(randomUUID())

    then:
    res == null
  }

  def "trusonaficationResultFromResponse should map a TrusonaficationResponse to a TrusonaficationResult"() {
    when:
    def res = sut.trusonaficationResultFromResponse(new TrusonaficationResponse(
      id: fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"),
      status: "ACCEPTED",
      userIdentifier: "sealz",
      createdAt: dateFormat.parse("2018-01-23T23:28:45Z"),
      updatedAt: dateFormat.parse("2018-01-23T23:28:46Z"),
      deviceIdentifier: "wall-e",
      desiredLevel: 2,
      action: "dig up",
      resource: "your lawn",
      expiresAt: dateFormat.parse("2018-01-23T23:28:47Z"),
      userPresence: true,
      prompt: true,
      authenticatorType: 'MOBILE_APP',
      result: new TrusonaficationResultResponse(
        id: randomUUID(),
        accepted: true,
        acceptedLevel: 2,
        boundUserIdentifier: "wall.e"
      )
    ))

    then:
    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == ACCEPTED
    res.userIdentifier == 'sealz'
    res.boundUserIdentifier == 'wall.e'
    res.createdAt.present
    res.updatedAt.present
    res.authenticatorType == MOBILE_APP
  }

  def "trusonaficationResultFromResponse should use the trusona id for the user identifier"() {
    when:
    def res = sut.trusonaficationResultFromResponse(new TrusonaficationResponse(
      id: fromString("96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9"),
      status: "ACCEPTED",
      trusonaId: "123456789",
      createdAt: dateFormat.parse("2018-01-23T23:28:45Z"),
      updatedAt: dateFormat.parse("2018-01-23T23:28:46Z"),
      deviceIdentifier: "wall-e",
      desiredLevel: 2,
      action: "dig up",
      resource: "your lawn",
      expiresAt: dateFormat.parse("2018-01-23T23:28:47Z"),
      userPresence: true,
      prompt: true,
      result: new TrusonaficationResultResponse(
        id: randomUUID(),
        accepted: true,
        acceptedLevel: 2,
        boundUserIdentifier: null
      )
    ))

    then:
    res.trusonaficationId == fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9')
    res.status == ACCEPTED
    res.userIdentifier == 'trusonaId:123456789'
    res.boundUserIdentifier == null
    res.createdAt.present
    res.updatedAt.present
  }
}
