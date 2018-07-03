package com.trusona.sdk.http.client.v2.response

import com.trusona.sdk.http.client.RequestResponseSpec

class ErrorResponseSpec extends RequestResponseSpec<ErrorResponse> {

  ErrorResponse sut = new ErrorResponse(
    "error",
    "message",
    "description",
    [foo: ["bar", "fizz"]]
  )

  String json = """
  {
    "error": "error",
    "message": "message",
    "description": "description",
    "field_errors": {
      "foo": [ "bar", "fizz" ]
    }
  }
  """
}
