package com.trusona.sdk.http

import okhttp3.MediaType
import okhttp3.ResponseBody

class Fixtures {
  static def jsonBody(String content) {
    return ResponseBody.create(
      MediaType.parse("application/json; charset=utf-8"),
      content
    )
  }
}
