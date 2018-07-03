package com.trusona.sdk.http.client.security;

import java.io.IOException;

public interface HmacMessage {

  String getBodyDigest() throws IOException;

  String getContentType();

  String getDate();

  String getMethod();

  String getRequestUri();
}