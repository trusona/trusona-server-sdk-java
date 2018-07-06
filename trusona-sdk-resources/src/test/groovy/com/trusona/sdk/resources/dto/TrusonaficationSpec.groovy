package com.trusona.sdk.resources.dto

import static com.trusona.sdk.config.JacksonConfig.getDateFormat

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
class TrusonaficationSpec extends DtoSpec<Trusonafication> {

  Trusonafication sut = new Trusonafication(
    deviceIdentifier: 'datDevice',
    userIdentifier: 'datUser',
    truCodeId: UUID.fromString("3827D5E5-B6C1-49F8-865E-72794D10BEF4"),
    desiredLevel: 2,
    action: 'partay',
    resource: 'your hauz',
    expiresAt: dateFormat.parse('2018-01-23T23:28:45Z'),
    callbackUrl: 'https://kid-and-play.com/',
    userPresence: false,
    prompt: false,
    showIdentityDocument: true
  )

  String json = """\
  {
    "device_identifier": "datDevice",
    "user_identifier": "datUser",
    "trucode_id": "3827D5E5-B6C1-49F8-865E-72794D10BEF4",
    "desired_level": 2,
    "action": "partay",
    "resource": "your hauz",
    "expires_at": "2018-01-23T23:28:45Z",
    "callback_url": "https://kid-and-play.com/",
    "user_presence": false,
    "prompt": false,
    "show_identity_document": true
  }
  """

  def "should default userPresence to true"() {
    when:
    def sut = Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    sut.userPresence
  }

  def "should default prompt to true"() {
    when:
    def sut = Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    sut.prompt
  }

  def "should default callbackUrl to null"() {
    when:
    def sut = Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    sut.callbackUrl == null
  }

  def "should default expiresAt to null"() {
    when:
    def sut = Trusonafication.essential()
      .deviceIdentifier('wall-e')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    sut.expiresAt == null
  }

  def "should default showIdentityDocument to false"() {
    when:
    def sut = Trusonafication.essential()
      .deviceIdentifier('wall-eeeee')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    !sut.showIdentityDocument
  }

  def "should default showIdentityDocument to true when executive"() {
    when:
    def sut = Trusonafication.executive()
      .deviceIdentifier('wall-eeeee')
      .action('jackson')
      .resource('humans')
      .build()

    then:
    sut.showIdentityDocument
  }

}
