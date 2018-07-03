package com.trusona.sdk.http.client.v2.response

import com.trusona.sdk.http.client.RequestResponseSpec

/**
 * Copyright Trusona, Inc.
 * Created on 1/22/18 for trusona-server-sdk.
 */
class TrusonaficationResultResponseSpec extends RequestResponseSpec<TrusonaficationResultResponse> {

  TrusonaficationResultResponse sut = new TrusonaficationResultResponse(
    id: UUID.fromString('96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8'),
    accepted: true,
    acceptedLevel: 2
  )

  String json = """\
  {
    "id": "96ea5830-8e5e-42c5-9cbb-8a941d2ff7f8",
    "is_accepted": true,
    "accepted_level": 2
  }
  """

}
