package com.trusona.sdk.http.client.security;

import com.trusona.sdk.http.Headers;
import okhttp3.Response;
import okio.ByteString;

import java.io.IOException;

public class ResponseHmacMessage implements HmacMessage {
  private Response response;
  private static final ByteString EMPTY = ByteString.encodeUtf8("");

  public ResponseHmacMessage(Response response) {
    this.response = response;
  }

  @Override
  public String getBodyDigest() throws IOException {
    return response.body() != null ?
      response.peekBody(Long.MAX_VALUE).source().buffer().md5().hex() :
      EMPTY.md5().hex();
  }

  @Override
  public String getContentType() {
    return response.header(Headers.CONTENT_TYPE, EMPTY.utf8());
  }

  @Override
  public String getDate() {
    return response.header(Headers.X_DATE);
  }

  @Override
  public String getMethod() {
    return response.request().method();
  }

  @Override
  public String getRequestUri() {
    String requestUri = response.request().url().encodedPath();
    String query = response.request().url().encodedQuery();

    if (query != null) {
      requestUri = requestUri + "?" + query;
    }

    return requestUri;
  }
}
