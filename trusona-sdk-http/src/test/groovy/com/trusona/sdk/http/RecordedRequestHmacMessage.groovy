package com.trusona.sdk.http

import com.trusona.sdk.http.client.security.HmacMessage
import okhttp3.mockwebserver.RecordedRequest

class RecordedRequestHmacMessage implements HmacMessage {
  private RecordedRequest recordedRequest;

  RecordedRequestHmacMessage(RecordedRequest recordedRequest) {
    this.recordedRequest = recordedRequest
  }

  @Override
  String getBodyDigest() throws IOException {
    return recordedRequest.getBody().md5().hex()
  }

  @Override
  String getContentType() {
    return recordedRequest.getHeader("Content-Type")
  }

  @Override
  String getDate() {
    return recordedRequest.getHeader("X-Date")
  }

  @Override
  String getMethod() {
    return recordedRequest.getMethod()
  }

  @Override
  String getRequestUri() {
    String uri = recordedRequest.getRequestUrl().encodedPath()
    String query = recordedRequest.getRequestUrl().encodedQuery()

    if (query != null) {
      uri = uri + "?" + query
    }

    return uri
  }
}
