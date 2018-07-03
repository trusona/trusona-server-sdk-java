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
    callbackUrl: 'https://kid-and-play.com/',
    userPresence: false,
    prompt: false,
    result: new TrusonaficationResultResponse(
      id: UUID.fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8'),
      accepted: true,
      acceptedLevel: 2
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
    "callback_url": "https://kid-and-play.com/",
    "user_presence": false,
    "prompt": false,
    "result": {
      "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8",
      "is_accepted": true,
      "accepted_level": 2
    }
  }
  """

}
