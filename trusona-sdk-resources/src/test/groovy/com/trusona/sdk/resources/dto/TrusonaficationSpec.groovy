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
    emailAddress: 'african-tiger@taco.net',
    truCodeId: UUID.fromString("3827D5E5-B6C1-49F8-865E-72794D10BEF4"),
    trusonaId: '123456789',
    desiredLevel: 2,
    action: 'partay',
    resource: 'your hauz',
    expiresAt: dateFormat.parse('2018-01-23T23:28:45Z'),
    userPresence: false,
    prompt: false,
    showIdentityDocument: true,
    customFields: ["foo": "bar"]
  )

  String json = """\
  {
    "device_identifier": "datDevice",
    "user_identifier": "datUser",
    "trucode_id": "3827D5E5-B6C1-49F8-865E-72794D10BEF4",
    "trusona_id": "123456789",
    "desired_level": 2,
    "action": "partay",
    "resource": "your hauz",
    "expires_at": "2018-01-23T23:28:45Z",
    "user_presence": false,
    "email": "african-tiger@taco.net",
    "prompt": false,
    "show_identity_document": true,
    "custom_fields": {
      "foo": "bar"
    }
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
