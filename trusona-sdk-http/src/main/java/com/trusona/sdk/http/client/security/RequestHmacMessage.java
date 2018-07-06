package com.trusona.sdk.http.client.security;

import com.trusona.sdk.http.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

import java.io.IOException;

public class RequestHmacMessage implements HmacMessage {

  private final Request request;

  public RequestHmacMessage(Request request) {
    this.request = request;
  }

  @Override
  public String getBodyDigest() throws IOException {
    RequestBody requestBody = request.body();
    Buffer sink = new Buffer();

    if (requestBody != null) {
      requestBody.writeTo(sink);
    }

    String value = sink.md5().hex();

    sink.clear();
    sink.close();

    return value;
  }

  @Override
  public String getContentType() {
    return request.header(Headers.CONTENT_TYPE);
  }

  @Override
  public String getDate() {
    return request.header(Headers.X_DATE);
  }

  @Override
  public String getMethod() {
    return request.method();
  }

  @Override
  public String getRequestUri() {
    String requestUri = request.url().encodedPath();
    String query = request.url().encodedQuery();

    if (query != null) {
      requestUri = requestUri + "?" + query;
    }

    return requestUri;
  }
}
