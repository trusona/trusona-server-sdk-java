package com.trusona.sdk.http.client.v2.response

import com.trusona.sdk.http.client.RequestResponseSpec

import static com.trusona.sdk.config.JacksonConfig.dateFormat
/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
class TrusonaficationResponseSpec extends RequestResponseSpec<TrusonaficationResponse> {

  TrusonaficationResponse sut = new TrusonaficationResponse(
    id: UUID.fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9'),
    status: 'ACCEPTED',
    userIdentifier: 't-money',
    createdAt: dateFormat.parse('2018-01-23T23:28:45Z'),
    updatedAt: dateFormat.parse('2018-01-23T23:28:46Z'),
    deviceIdentifier: 'datDevice',
    desiredLevel: 2,
    action: 'partay',
    resource: 'your hauz',
    expiresAt: dateFormat.parse('2018-01-23T23:28:47Z'),
    userPresence: false,
    prompt: false,
    showIdentityDocument: true,
    result: new TrusonaficationResultResponse(
      id: UUID.fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8'),
      accepted: true,
      acceptedLevel: 2,
      boundUserIdentifier: 'dr.mantis.tobboggan'
    )
  )

  String json = """\
  {
    "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f9",
    "status": "ACCEPTED",
    "user_identifier": "t-money",
    "created_at": "2018-01-23T23:28:45Z",
    "updated_at": "2018-01-23T23:28:46Z",
    "device_identifier": "datDevice",
    "desired_level": 2,
    "action": "partay",
    "resource": "your hauz",
    "expires_at": "2018-01-23T23:28:47Z",
    "user_presence": false,
    "prompt": false,
    "show_identity_document": true,
    "result": {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8",
      "is_accepted": true,
      "accepted_level": 2,
      "bound_user_identifier": "dr.mantis.tobboggan"
    }
  }
  """

  def "should default userPresence to true"() {
    given:
    def sut = new TrusonaficationResponse()

    when:
    def res = sut.userPresence

    then:
    res
  }

  def "should default prompt to true"() {
    given:
    def sut = new TrusonaficationResponse()

    when:
    def res = sut.prompt

    then:
    res
  }

  def "should default showIdentityDocument to false"() {
    given:
    def sut = new TrusonaficationResponse()

    when:
    def res = sut.showIdentityDocument

    then:
    !res
  }

}
