package com.trusona.sdk.http.client.v2.request

import com.trusona.sdk.http.client.RequestResponseSpec

import static com.trusona.sdk.config.JacksonConfig.dateFormat

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
class TrusonaficationRequestSpec extends RequestResponseSpec<TrusonaficationRequest> {

  TrusonaficationRequest sut = new TrusonaficationRequest(
    deviceIdentifier: 'datDevice',
    userIdentifier: 'datUser',
    desiredLevel: 2,
    action: 'partay',
    resource: 'your hauz',
    expiresAt: dateFormat.parse('2018-01-23T23:28:45Z'),
    userPresence: false,
    prompt: false,
    showIdentityDocument: true
  )

  String json = """\
  {
    "device_identifier": "datDevice",
    "user_identifier": "datUser",
    "desired_level": 2,
    "action": "partay",
    "resource": "your hauz",
    "expires_at": "2018-01-23T23:28:45Z",
    "user_presence": false,
    "prompt": false,
    "show_identity_document": true
  }
  """

  def "should default userPresence to true"() {
    given:
    def sut = new TrusonaficationRequest()

    when:
    def res = sut.userPresence

    then:
    res
  }

  def "should default prompt to true"() {
    given:
    def sut = new TrusonaficationRequest()

    when:
    def res = sut.prompt

    then:
    res
  }

  def "should default showIdentityDocument to false"() {
    given:
    def sut = new TrusonaficationRequest()

    when:
    def res = sut.showIdentityDocument

    then:
    !res
  }
}
